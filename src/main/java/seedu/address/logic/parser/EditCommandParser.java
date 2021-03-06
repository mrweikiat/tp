package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITYTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEIGHTAGE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_CODE,
                    PREFIX_DEADLINE_DATE, PREFIX_DEADLINE_TIME, PREFIX_WEIGHTAGE, PREFIX_NOTES, PREFIX_TAG,
                    PREFIX_PRIORITYTAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        EditTaskDescriptor editTaskDescriptor = new EditTaskDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editTaskDescriptor.setTaskName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_CODE).isPresent()) {
            editTaskDescriptor.setModuleCode(ParserUtil.parseCode(argMultimap.getValue(PREFIX_CODE).get()));
        }
        if (argMultimap.getValue(PREFIX_DEADLINE_DATE).isPresent()) {
            editTaskDescriptor.setDeadlineDate(ParserUtil
                    .parseDeadlineDate(argMultimap.getValue(PREFIX_DEADLINE_DATE).get()));
        }
        if (argMultimap.getValue(PREFIX_DEADLINE_TIME).isPresent()) {
            editTaskDescriptor.setDeadlineTime(ParserUtil
                    .parseDeadlineTime(argMultimap.getValue(PREFIX_DEADLINE_TIME).get()));
        }
        if (argMultimap.getValue(PREFIX_WEIGHTAGE).isPresent()) {
            editTaskDescriptor.setWeightage(ParserUtil
                    .parseWeightage(argMultimap.getValue(PREFIX_WEIGHTAGE).get()));
        }
        if (argMultimap.getValue(PREFIX_NOTES).isPresent()) {
            editTaskDescriptor.setNotes(ParserUtil
                    .parseNotes(argMultimap.getValue(PREFIX_NOTES).get()));
        }

        if (argMultimap.getValue(PREFIX_PRIORITYTAG).isPresent()) {
            editTaskDescriptor.setPriorityTag(ParserUtil
                    .parsePriorityTag(argMultimap.getValue(PREFIX_PRIORITYTAG).get()));


        }

        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editTaskDescriptor::setTags);

        if (!editTaskDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }


        return new EditCommand(index, editTaskDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
