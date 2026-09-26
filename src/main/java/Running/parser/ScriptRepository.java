package main.java.Running.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptRepository {

    private final File scriptsRoot;

    private final ScriptParser parser;

    private final Map<String, ScriptProgram> cache =
            new ConcurrentHashMap<>();


    public ScriptRepository(
            File scriptsRoot
    ) {

        if (
                scriptsRoot == null
                        || !scriptsRoot.isDirectory()
        ) {

            throw new IllegalArgumentException(
                    "无效的 scripts 目录："
                            + scriptsRoot
            );
        }


        this.scriptsRoot =
                scriptsRoot.getAbsoluteFile();

        this.parser =
                new ScriptParser();
    }


    // =========================================================
    // 加载脚本
    // =========================================================

    public ScriptProgram load(
            String scriptName
    ) throws IOException {

        String normalizedName =
                normalizeScriptName(
                        scriptName
                );


        ScriptProgram cached =
                cache.get(
                        normalizedName
                );


        if (cached != null) {

            return cached;
        }


        File scriptFile =
                resolveScript(
                        normalizedName
                );


        ScriptProgram program =
                parser.parseProgram(
                        scriptFile
                );


        cache.put(
                normalizedName,
                program
        );


        return program;
    }


    // =========================================================
    // 找脚本
    // =========================================================

    private File resolveScript(
            String scriptName
    ) throws IOException {

        Path root =
                scriptsRoot
                        .getCanonicalFile()
                        .toPath()
                        .normalize();


        Path target =
                root.resolve(
                        scriptName
                )
                .normalize();


        // 防止 ../ 越界访问项目外文件
        if (!target.startsWith(root)) {

            throw new IllegalArgumentException(
                    "非法脚本路径："
                            + scriptName
            );
        }


        File file =
                target.toFile();


        if (!file.isFile()) {

            throw new IOException(
                    "脚本不存在："
                            + scriptName
            );
        }


        return file;
    }


    // =========================================================
    // 规范化脚本名字
    // =========================================================

    private String normalizeScriptName(
            String scriptName
    ) {

        if (
                scriptName == null
                        || scriptName.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "脚本名称不能为空"
            );
        }


        String name =
                scriptName.trim();


        if (
                !name
                        .toLowerCase()
                        .endsWith(".txt")
        ) {

            name += ".txt";
        }


        return name;
    }


    // =========================================================
    // 清除缓存
    // =========================================================

    public void invalidate(
            String scriptName
    ) {

        cache.remove(
                normalizeScriptName(
                        scriptName
                )
        );
    }


    // =========================================================
    // 清空缓存
    // =========================================================

    public void clearCache() {

        cache.clear();
    }


    public File getScriptsRoot() {

        return scriptsRoot;
    }
}