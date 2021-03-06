package seedu.module.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.module.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.module.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.module.logic.commands.CommandTestUtil.VALID_DEADLINE_BOB;
import static seedu.module.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BOB;
import static seedu.module.logic.commands.CommandTestUtil.VALID_MODULE_BOB;
import static seedu.module.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.module.logic.commands.CommandTestUtil.VALID_TAG_HIGH;

import org.junit.jupiter.api.Test;

import seedu.module.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.module.testutil.EditTaskDescriptorBuilder;

public class EditTaskDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditTaskDescriptor descriptorWithSameValues = new EditTaskDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditTaskDescriptor editedAmy = new EditTaskDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different deadline -> returns false
        editedAmy = new EditTaskDescriptorBuilder(DESC_AMY).withDeadline(VALID_DEADLINE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different module -> returns false
        editedAmy = new EditTaskDescriptorBuilder(DESC_AMY).withModule(VALID_MODULE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different description -> returns false
        editedAmy = new EditTaskDescriptorBuilder(DESC_AMY).withDescription(VALID_DESCRIPTION_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditTaskDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HIGH).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }
}
