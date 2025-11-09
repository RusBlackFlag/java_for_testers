package tests;

import model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class ContactModificationTests extends TestBase {

    @Test
    void canModifyContact() {
        // предусловие — если нет контактов, создаём
        if (app.jdbc().getContactList().isEmpty()) {
            app.contacts().createContact(new ContactData().withFirstname("First").withLastname("Last"));
        }

        var oldContacts = app.jdbc().getContactList();
        var rnd = new Random();
        var index = rnd.nextInt(oldContacts.size());

        // данные для модификации
        var modifiedData = new ContactData()
                .withFirstname("ModifiedFirst")
                .withLastname("ModifiedLast");

        app.contacts().modifyContact(oldContacts.get(index), modifiedData);

        var newContacts = app.jdbc().getContactList();

        var expectedList = new ArrayList<>(oldContacts);
        // сохраним id старого контакта в новом объекте
        expectedList.set(index, modifiedData.withId(oldContacts.get(index).id()));

        // безопасный компаратор по id (если id нечисловой — fallback на строковое сравнение)
        Comparator<ContactData> compareById = (c1, c2) -> {
            try {
                return Integer.compare(Integer.parseInt(c1.id()), Integer.parseInt(c2.id()));
            } catch (NumberFormatException e) {
                return c1.id().compareTo(c2.id());
            }
        };

        newContacts.sort(compareById);
        expectedList.sort(compareById);

        Assertions.assertEquals(expectedList, newContacts);
    }
}
