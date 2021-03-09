package seedu.module.logic.parser;

import seedu.module.commons.core.index.Index;
import seedu.module.commons.exceptions.IllegalValueException;
import seedu.module.logic.commands.Command;
import seedu.module.logic.commands.TagCommand;
import seedu.module.logic.parser.exceptions.ParseException;
import seedu.module.model.tag.Tag;

import static java.util.Objects.requireNonNull;
import static seedu.module.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.module.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.module.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.module.logic.parser.CliSyntax.PREFIX_MODULE;
import static seedu.module.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.module.logic.parser.CliSyntax.PREFIX_TAG;

public class TagCommandParser implements Parser {
    @Override
    public Command parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME,
                PREFIX_DEADLINE, PREFIX_MODULE, PREFIX_DESCRIPTION, PREFIX_TAG);
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    TagCommand.MESSAGE_USAGE), ive);
        }

        if (argMultimap.getValue(PREFIX_TAG).isEmpty()) {
            throw new ParseException(TagCommand.MESSAGE_NOT_EDITED);
        }

        String tagName = argMultimap.getValue(PREFIX_TAG).orElse("");

        Tag tag;
        try {
            tag = new Tag(tagName);
        } catch (IllegalArgumentException e) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }

        return new TagCommand(index, tag);
    }

}
