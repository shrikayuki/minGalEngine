package main.java.Running.parser;

import main.java.Running.type.Choice;
import main.java.Running.type.OpCode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptParser {

    private static final Pattern CHOICE_PATTERN =
            Pattern.compile(
                    "^(\\d+)\\s+(.+?)\\s+->\\s+(.+)$"
            );

    private static final Set<String> COMMANDS =
            Set.of(
                    "SAY",
                    "CHOICE",
                    "LABEL",
                    "JUMP",
                    "CALL",
                    "RETURN",
                    "BG",
                    "CHAR",
                    "BGM",
                    "VOICE",
                    "END",
                    "ENDCHOICE"
            );


    // =========================================================
    // 解析整个脚本
    // =========================================================

    public ScriptProgram parseProgram(
            File file
    ) throws IOException {

        if (
                file == null
                        || !file.isFile()
        ) {

            throw new FileNotFoundException(
                    "Script not found: " + file
            );
        }

        List<String> lines;

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(file),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            lines =
                    reader.lines()
                            .toList();
        }

        List<Instruction> instructions =
                new ArrayList<>();

        Map<String, Integer> labels =
                new HashMap<>();


        // =====================================================
        // 逐行解析
        // =====================================================

        int index = 0;

        while (index < lines.size()) {

            String rawLine =
                    lines.get(index);

            String line =
                    rawLine.trim();

            index++;


            // -------------------------------------------------
            // 空行 / 注释
            // -------------------------------------------------

            if (
                    line.isEmpty()
                            || line.startsWith("#")
            ) {

                continue;
            }


            // -------------------------------------------------
            // 拆命令
            // -------------------------------------------------

            String[] parts =
                    line.split(
                            "\\s+",
                            2
                    );

            String command =
                    parts[0]
                            .toUpperCase(Locale.ROOT);

            String argument =
                    parts.length > 1
                            ? parts[1].trim()
                            : "";


            // =================================================
            // 命令
            // =================================================

            switch (command) {

                // -------------------------------------------------
                // SAY
                // -------------------------------------------------

                case "SAY" -> {

                    requireArgument(
                            command,
                            argument,
                            file
                    );

                    instructions.add(
                            new Instruction(
                                    OpCode.SAY,
                                    argument
                            )
                    );
                }


                // -------------------------------------------------
                // LABEL
                // -------------------------------------------------

                case "LABEL" -> {

                    requireArgument(
                            command,
                            argument,
                            file
                    );

                    if (
                            labels.containsKey(
                                    argument
                            )
                    ) {

                        throw new IllegalArgumentException(
                                "Duplicate LABEL: "
                                        + argument
                                        + " in "
                                        + file
                        );
                    }

                    labels.put(
                            argument,
                            instructions.size()
                    );

                    instructions.add(
                            new Instruction(
                                    OpCode.LABEL,
                                    argument
                            )
                    );
                }


                // -------------------------------------------------
                // JUMP
                // -------------------------------------------------

                case "JUMP" -> {

                    requireArgument(
                            command,
                            argument,
                            file
                    );

                    instructions.add(
                            new Instruction(
                                    OpCode.JUMP,
                                    argument
                            )
                    );
                }


                // -------------------------------------------------
                // BG
                // -------------------------------------------------

                case "BG" -> {

                    requireArgument(
                            command,
                            argument,
                            file
                    );

                    instructions.add(
                            new Instruction(
                                    OpCode.BG,
                                    argument
                            )
                    );
                }


                // -------------------------------------------------
                // CHAR
                // -------------------------------------------------

                case "CHAR" -> {

                    requireArgument(
                            command,
                            argument,
                            file
                    );

                    instructions.add(
                            new Instruction(
                                    OpCode.CHAR,
                                    argument
                            )
                    );
                }


                // -------------------------------------------------
                // BGM
                // -------------------------------------------------

                case "BGM" -> {

                    requireArgument(
                            command,
                            argument,
                            file
                    );

                    instructions.add(
                            new Instruction(
                                    OpCode.BGM,
                                    argument
                            )
                    );
                }


                // -------------------------------------------------
                // VOICE
                // -------------------------------------------------

                case "VOICE" -> {

                    requireArgument(
                            command,
                            argument,
                            file
                    );

                    instructions.add(
                            new Instruction(
                                    OpCode.VOICE,
                                    argument
                            )
                    );
                }


                // -------------------------------------------------
                // END
                // -------------------------------------------------

                case "END" -> {

                    instructions.add(
                            new Instruction(
                                    OpCode.END
                            )
                    );
                }


                // -------------------------------------------------
                // CHOICE
                // -------------------------------------------------

                case "CHOICE" -> {

                    ChoiceResult result =
                            parseChoices(
                                    lines,
                                    index,
                                    file
                            );

                    instructions.add(
                            new Instruction(
                                    OpCode.CHOICE,
                                    result.choices()
                            )
                    );

                    index =
                            result.nextIndex();
                }

                // -------------------------------------------------
// CALL
// -------------------------------------------------

                case "CALL" -> {

                    requireArgument(
                            command,
                            argument,
                            file
                    );


                    instructions.add(
                            new Instruction(
                                    OpCode.CALL,
                                    argument
                            )
                    );
                }


                // -------------------------------------------------
                // RETURN
                // -------------------------------------------------

                case "RETURN" -> {

                    if (!argument.isBlank()) {

                        throw new IllegalArgumentException(
                                "RETURN 不接受参数: "
                                        + file
                        );
                    }


                    instructions.add(
                            new Instruction(
                                    OpCode.RETURN
                            )
                    );
                }


                // -------------------------------------------------
                // ENDCHOICE
                // -------------------------------------------------

                case "ENDCHOICE" -> {

                    throw new IllegalArgumentException(
                            "ENDCHOICE 没有对应的 CHOICE: "
                                    + file
                    );
                }


                // -------------------------------------------------
                // 未知命令
                // -------------------------------------------------

                default -> {

                    throw new IllegalArgumentException(
                            "Unknown command: "
                                    + command
                                    + " in "
                                    + file
                    );
                }
            }
        }

        return new ScriptProgram(
                instructions,
                labels
        );
    }


    // =========================================================
    // 兼容旧接口
    // =========================================================

    public List<Instruction> parse(
            File file
    ) throws IOException {

        return parseProgram(
                file
        ).getInstructions();
    }


    // =========================================================
    // 解析 Choice
    // =========================================================

    private ChoiceResult parseChoices(
            List<String> lines,
            int startIndex,
            File file
    ) {

        List<Choice> choices =
                new ArrayList<>();

        int index =
                startIndex;


        while (index < lines.size()) {

            String rawLine =
                    lines.get(index);

            String line =
                    rawLine.trim();


            // -------------------------------------------------
            // 空行
            // -------------------------------------------------

            if (line.isEmpty()) {

                index++;

                continue;
            }


            // -------------------------------------------------
            // 注释
            // -------------------------------------------------

            if (line.startsWith("#")) {

                index++;

                continue;
            }


            // -------------------------------------------------
            // 显式结束
            // -------------------------------------------------

            if (
                    line.equalsIgnoreCase(
                            "ENDCHOICE"
                    )
            ) {

                index++;

                break;
            }


            // -------------------------------------------------
            // 判断是不是新的脚本命令
            // -------------------------------------------------

            String[] parts =
                    line.split(
                            "\\s+",
                            2
                    );

            String possibleCommand =
                    parts[0]
                            .toUpperCase(
                                    Locale.ROOT
                            );


            /*
             * 没写 ENDCHOICE 时：
             *
             * CHOICE
             * 1 ...
             * 2 ...
             *
             * LABEL xxx
             *
             * 这里看到 LABEL，
             * 就认为 CHOICE 已经结束。
             */

            if (
                    COMMANDS.contains(
                            possibleCommand
                    )
                            && !possibleCommand.equals(
                            "ENDCHOICE"
                    )
            ) {

                break;
            }


            // -------------------------------------------------
            // 解析 Choice
            // -------------------------------------------------

            Matcher matcher =
                    CHOICE_PATTERN.matcher(
                            line
                    );

            if (
                    !matcher.matches()
            ) {

                throw new IllegalArgumentException(
                        "Invalid CHOICE line: "
                                + line
                                + " in "
                                + file
                );
            }


            choices.add(
                    new Choice(
                            matcher.group(2),
                            matcher.group(3)
                    )
            );

            index++;
        }


        // =====================================================
        // 至少一个选择
        // =====================================================

        if (
                choices.isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "CHOICE has no options in "
                            + file
            );
        }

        return new ChoiceResult(
                choices,
                index
        );
    }


    // =========================================================
    // 参数检查
    // =========================================================

    private void requireArgument(
            String command,
            String argument,
            File file
    ) {

        if (
                argument.isBlank()
        ) {

            throw new IllegalArgumentException(
                    command
                            + " requires an argument in "
                            + file
            );
        }
    }


    // =========================================================
    // Choice 解析结果
    // =========================================================

    private record ChoiceResult(
            List<Choice> choices,
            int nextIndex
    ) {
    }
}