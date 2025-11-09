package tests;

import model.ContactData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ContactInfoTests extends TestBase {

//    @Test
//    void testPhones() {
//        var contacts = app.hbm().getContactList();
//        var expected = contacts.stream().collect(Collectors.toMap(ContactData::id, contact ->
//            Stream.of(contact.home(), contact.mobile(), contact.work(), contact.secondary())
//                    .filter(s -> s != null && ! "".equals(s))
//                    .collect(Collectors.joining("\n"))
//        ));
//        var phones = app.contacts().getPhones();
//        Assertions.assertEquals(expected, phones);
//    }

    @Test
    void testContactInfo() {
        // получаем первый контакт с главной страницы
        var contact = app.contacts().all().getFirst();

        // получаем данные этого же контакта из формы редактирования
        var contactInfoFromEditForm =
                app.contacts().infoFromEditForm(Integer.parseInt(contact.id()));

        // объединяем телефоны из формы редактирования
        var mergedPhones = mergePhones(contactInfoFromEditForm);
        var phonesFromMainPage = app.contacts().allPhonesFor(contact);

        Assertions.assertEquals(
                mergedPhones,
                cleaned(phonesFromMainPage),
                "Телефоны на главной странице отличаются от данных формы");

        // Проверка адреса
        Assertions.assertEquals(
                contactInfoFromEditForm.address().trim(),
                contact.address().trim(),
                "Адрес на главной странице отличается");

        // Проверка email’ов
        var mergedEmails = mergeEmails(contactInfoFromEditForm);
        var emailsFromMainPage = app.contacts().allEmailsFor(contact);

        Assertions.assertEquals(
                mergedEmails,
                emailsFromMainPage,
                "E-mail на главной странице отличается");
    }

    private String mergePhones(ContactData contact) {
        return Arrays.asList(contact.home(), contact.mobile(), contact.work(), contact.secondary())
                .stream()
                .filter(s -> s != null && !s.isBlank())
                .map(ContactInfoTests::cleaned)
                .collect(Collectors.joining("\n"));
    }

    private String mergeEmails(ContactData contact) {
        return Arrays.asList(contact.email(), contact.email2(), contact.email3())
                .stream()
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining("\n"));
    }

    private static String cleaned(String phone) {
        return phone.replaceAll("\\s", "").replaceAll("[-()]", "");
    }
}
