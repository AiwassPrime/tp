package seedu.module.logic.parser;

import static seedu.module.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.module.logic.commands.CommandTestUtil.DEADLINE_DESC_AMY;
import static seedu.module.logic.commands.CommandTestUtil.DEADLINE_DESC_BOB;
import static seedu.module.logic.commands.CommandTestUtil.DESCRIPTION_DESC_AMY;
import static seedu.module.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BOB;
import static seedu.module.logic.commands.CommandTestUtil.INVALID_DEADLINE_DESC;
import static seedu.module.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.module.logic.commands.CommandTestUtil.INVALID_MODULE_DESC;
import static seedu.module.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.module.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.module.logic.commands.CommandTestUtil.MODULE_DESC_AMY;
import static seedu.module.logic.commands.CommandTestUtil.MODULE_DESC_BOB;
import static seedu.module.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.module.logic.commands.CommandTestUtil.TAG_DESC_HIGH;
import static seedu.module.logic.commands.CommandTestUtil.TAG_DESC_LOW;
import static seedu.module.logic.commands.CommandTestUtil.VALID_DEADLINE_AMY;
import static seedu.module.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
import static seedu.module.logic.commands.CommandTestUtil.VALID_DESCRIPTION_AMY;
import static seedu.module.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.module.logic.commands.CommandTestUtil.VALID_MODULE_AMY;
import static seedu.module.logic.commands.CommandTestUtil.VALID_MODULE_BOB;
import static seedu.module.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.module.logic.commands.CommandTestUtil.VALID_TAG_HIGH;
import static seedu.module.logic.commands.CommandTestUtil.VALID_TAG_LOW;
import static seedu.module.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.module.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.module.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.module.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.module.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.module.testutil.TypicalIndexes.INDEX_THIRD_TASK;

import org.junit.jupiter.api.Test;

import seedu.module.commons.core.index.Index;
import seedu.module.logic.commands.EditCommand;
import seedu.module.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.module.model.tag.Tag;
import seedu.module.model.task.Deadline;
import seedu.module.model.task.Description;
import seedu.module.model.task.Module;
import seedu.module.model.task.Name;
import seedu.module.testutil.EditTaskDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_DEADLINE_DESC, Deadline.MESSAGE_CONSTRAINTS); // invalid deadline
        assertParseFailure(parser, "1" + INVALID_MODULE_DESC, Module.MESSAGE_CONSTRAINTS); // invalid module
        // invalid description
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC, Description.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid deadline followed by valid module
        assertParseFailure(parser, "1" + INVALID_DEADLINE_DESC + MODULE_DESC_AMY, Deadline.MESSAGE_CONSTRAINTS);

        // valid deadline followed by invalid deadline. The test case for invalid deadline followed by valid deadline
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + DEADLINE_DESC_BOB + INVALID_DEADLINE_DESC, Deadline.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Task} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_LOW + TAG_DESC_HIGH + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_LOW + TAG_EMPTY + TAG_DESC_HIGH, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_LOW + TAG_DESC_HIGH, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_MODULE_DESC
                + VALID_DESCRIPTION_AMY + VALID_DEADLINE_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_TASK;
        String userInput = targetIndex.getOneBased() + DEADLINE_DESC_BOB + TAG_DESC_HIGH
                + MODULE_DESC_AMY + DESCRIPTION_DESC_AMY + NAME_DESC_AMY + TAG_DESC_LOW;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_AMY)
                .withDeadline(VALID_DEADLINE_BOB).withModule(VALID_MODULE_AMY).withDescription(VALID_DESCRIPTION_AMY)
                .withTags(VALID_TAG_HIGH, VALID_TAG_LOW).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + DEADLINE_DESC_BOB + MODULE_DESC_AMY;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDeadline(VALID_DEADLINE_BOB)
                .withModule(VALID_MODULE_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // deadline
        userInput = targetIndex.getOneBased() + DEADLINE_DESC_AMY;
        descriptor = new EditTaskDescriptorBuilder().withDeadline(VALID_DEADLINE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // module
        userInput = targetIndex.getOneBased() + MODULE_DESC_AMY;
        descriptor = new EditTaskDescriptorBuilder().withModule(VALID_MODULE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_AMY;
        descriptor = new EditTaskDescriptorBuilder().withDescription(VALID_DESCRIPTION_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_LOW;
        descriptor = new EditTaskDescriptorBuilder().withTags(VALID_TAG_LOW).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + DEADLINE_DESC_AMY + DESCRIPTION_DESC_AMY + MODULE_DESC_AMY
                + TAG_DESC_LOW + DEADLINE_DESC_AMY + DESCRIPTION_DESC_AMY + MODULE_DESC_AMY + TAG_DESC_LOW
                + DEADLINE_DESC_BOB + DESCRIPTION_DESC_BOB + MODULE_DESC_BOB + TAG_DESC_HIGH;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDeadline(VALID_DEADLINE_BOB)
                .withModule(VALID_MODULE_BOB).withDescription(VALID_DESCRIPTION_BOB)
                .withTags(VALID_TAG_LOW, VALID_TAG_HIGH)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + INVALID_DEADLINE_DESC + DEADLINE_DESC_BOB;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withDeadline(VALID_DEADLINE_BOB).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + MODULE_DESC_BOB + INVALID_DEADLINE_DESC + DESCRIPTION_DESC_BOB
                + DEADLINE_DESC_BOB;
        descriptor = new EditTaskDescriptorBuilder().withDeadline(VALID_DEADLINE_BOB).withModule(VALID_MODULE_BOB)
                .withDescription(VALID_DESCRIPTION_BOB).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
