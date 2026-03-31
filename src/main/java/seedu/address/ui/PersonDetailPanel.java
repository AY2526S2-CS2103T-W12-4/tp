package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

import java.time.LocalDate;
import java.util.Comparator;

import static seedu.address.ui.PersonCard.setShown;

/**
 * An UI component that displays information of a {@code Person} in View mode.
 */
public class PersonDetailPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailPanel.fxml";

    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label address;
    @FXML
    private FlowPane tags;
    @FXML
    private Label notes;
    @FXML
    private Label followUpDate;
    @FXML
    private Label circleBadge;

    /**
     * Creates a {@code PersonDetailPanel} with the given {@code Person} to display.
     */
    public PersonDetailPanel(Person person) {
        super(FXML);

        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        address.setText(person.getAddress().value);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        notes.setText(person.getNotes()
                .map(Note::toString)
                .orElse("-"));

        if (person.getFollowUpDate().isPresent()) {
            LocalDate date = person.getFollowUpDate().get().value;
            followUpDate.setText(date.toString());

            LocalDate today = LocalDate.now();
            LocalDate soonThreshold = today.plusDays(3);
            if (!date.isBefore(today) && !date.isAfter(soonThreshold)) {
                followUpDate.getStyleClass().add("follow-up-soon");
            }
        } else {
            followUpDate.setText("-");
        }

        person.getCircle().ifPresentOrElse(c -> {
            String circle = c.trim().toLowerCase();
            circleBadge.setText(circle);
            setShown(circleBadge, true);

            switch (circle) {
                case "client":
                    circleBadge.getStyleClass().add("circle-client");
                    break;
                case "prospect":
                    circleBadge.getStyleClass().add("circle-prospect");
                    break;
                case "friend":
                    circleBadge.getStyleClass().add("circle-friend");
                    break;
                default:
                    break;
            }
        }, () -> setShown(circleBadge, false));
    }
}
