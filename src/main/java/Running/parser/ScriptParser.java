package main.java.Running.parser;

import main.java.Running.type.Choice;
import main.java.Running.type.OpCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ScriptParser {

    public List<Instruction> parse(Path path)
            throws IOException {

        List<Instruction> program =
                new ArrayList<>();

        List<String> lines =
                Files.readAllLines(path);

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            // SAY
            if (line.startsWith("SAY ")) {

                String text = line.substring(4);

                program.add(
                        new Instruction(
                                OpCode.SAY,
                                new Object[]{text}
                        )
                );
            }

            // CHOICE
            else if (line.equals("CHOICE")) {

                List<Choice> choices =
                        new ArrayList<>();

                i++;

                while (i < lines.size()) {

                    String option =
                            lines.get(i).trim();

                    if (option.isEmpty()) {
                        i++;
                        continue;
                    }

                    if (!option.matches(
                            "\\d+ .+ -> .+")) {
                        i--;
                        break;
                    }

                    String[] parts =
                            option.split("->", 2);

                    String left =
                            parts[0].trim();

                    String target =
                            parts[1].trim();

                    String text =
                            left.substring(
                                    left.indexOf(" ") + 1
                            );

                    choices.add(
                            new Choice(text, target)
                    );

                    i++;
                }

                program.add(
                        new Instruction(
                                OpCode.CHOICE,
                                new Object[]{choices}
                        )
                );
            }

            // LABEL
            else if (line.startsWith("LABEL ")) {

                String label =
                        line.substring(6).trim();

                program.add(
                        new Instruction(
                                OpCode.LABEL,
                                new Object[]{label}
                        )
                );
            }

            // JUMP
            else if (line.startsWith("JUMP ")) {

                String target =
                        line.substring(5).trim();

                program.add(
                        new Instruction(
                                OpCode.JUMP,
                                new Object[]{target}
                        )
                );
            }
            // BG
            else if (line.startsWith("BG ")) {

                String image =
                        line.substring(3).trim();

                program.add(
                        new Instruction(
                                OpCode.BG,
                                new Object[]{image}
                        )
                );
            }

            // BGM
            else if (line.startsWith("BGM ")) {

                String music =
                        line.substring(4).trim();

                program.add(
                        new Instruction(
                                OpCode.BGM,
                                new Object[]{music}
                        )
                );
            }

            // VOICE
            else if (line.startsWith("VOICE ")) {

                String voice =
                        line.substring(6).trim();

                program.add(
                        new Instruction(
                                OpCode.VOICE,
                                new Object[]{voice}
                        )
                );
            }

// CHAR
            else if (line.startsWith("CHAR ")) {

                String image =
                        line.substring(5).trim();

                program.add(
                        new Instruction(
                                OpCode.CHAR,
                                new Object[]{image}
                        )
                );
            }

            // END
            else if (line.equals("END")) {

                program.add(
                        new Instruction(
                                OpCode.END,
                                new Object[]{}
                        )
                );
            }

            else {

                throw new RuntimeException(
                        "Unknown command: " + line
                );
            }
        }

        return program;
    }
}