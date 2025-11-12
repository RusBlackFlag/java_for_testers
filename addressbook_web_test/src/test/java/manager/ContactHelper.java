package manager;

import model.ContactData;
import model.GroupData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactHelper extends HelperBase {

    public ContactHelper(ApplicationManager manager) {
        super(manager);
    }
    private final Map<String, String> cachedPhones = new HashMap<>();
    private final Map<String, String> cachedEmails = new HashMap<>();

    public String allPhonesFor(ContactData contact) {
        return cachedPhones.get(contact.id());
    }

    public String allEmailsFor(ContactData contact) {
        return cachedEmails.get(contact.id());
    }

    public void createContact(ContactData contact) {
        System.out.println("DEBUG: создаём контакт -> " + contact);
        click(By.linkText("add new"));
        type(By.name("firstname"), contact.firstname());
        type(By.name("middlename"), contact.middlename());
        type(By.name("lastname"), contact.lastname());
        type(By.name("nickname"), contact.nickname());
//        attach(By.name("photo"), contact.photo());
        click(By.cssSelector("input:nth-child(75)")); // кнопка submit
        click(By.linkText("home"));
    }


    public void createContact(ContactData contact, GroupData group) {
        System.out.println("DEBUG: создаём контакт -> " + contact);
        click(By.linkText("add new"));
        type(By.name("firstname"), contact.firstname());
        type(By.name("lastname"), contact.lastname());
        selectGroup(group);
        click(By.cssSelector("input:nth-child(75)")); // кнопка submit
        click(By.linkText("home"));
    }

    private void selectGroup(GroupData group) {
        new Select(manager.driver.findElement(By.name("new_group"))).selectByValue(group.id());
    }

    //Не работает добавление и удаление контакта в группу у меня в приложении. Фиг знает почему
    // пишу наощупь
    public void addContactToGroup(ContactData contact, GroupData group) {
        selectContactById(contact.id());
        new Select(manager.driver.findElement(By.name("to_group"))).selectByVisibleText(group.name());
        manager.driver.findElement(By.name("add")).click();
    }

    public void removeContactFromGroup(ContactData contact, GroupData group) {
        new Select(manager.driver.findElement(By.name("group"))).selectByVisibleText(group.name());
        selectContactById(contact.id());
        manager.driver.findElement(By.name("remove")).click();
    }

    private void selectContactById(String id) {
        manager.driver.findElement(By.cssSelector("input[value='" + id + "']")).click();
    }

    private void openEditFormById(int id) {
        manager.driver.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click();
    }

    public void deleteContact(int index) {
        List<WebElement> checkboxes = manager.driver.findElements(By.name("selected[]"));

        if (index < 0 || index >= checkboxes.size()) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }

        // клик по нужному чекбоксу
        checkboxes.get(index).click();

        // клик по кнопке Delete
        manager.driver.findElement(By.xpath("//input[@value='Delete']")).click();

        // подтвердить alert (если есть)
        new WebDriverWait(manager.driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent()).accept();

        // ждать, пока количество контактов уменьшится на 1
        new WebDriverWait(manager.driver, Duration.ofSeconds(5))
                .until(d -> manager.driver.findElements(By.name("selected[]")).size() == checkboxes.size() - 1);
    }


    private int getContactsCount() {
        return Integer.parseInt(manager.driver.findElement(By.id("search_count")).getText());
    }

    public void modifyContact(ContactData contact, ContactData modifiedContact) {
        // открываем страницу со списком контактов
        click(By.linkText("home"));

        // выбираем контакт по id
        selectContact(contact);

        // кликаем "Edit"
        click(By.cssSelector("img[title='Edit']"));

        // заполняем поля новыми данными
        type(By.name("firstname"), modifiedContact.firstname());
        type(By.name("middlename"), modifiedContact.middlename());
        type(By.name("lastname"), modifiedContact.lastname());
        type(By.name("nickname"), modifiedContact.nickname());

        // сохраняем
        click(By.name("update"));

        // возвращаемся на домашнюю страницу
        click(By.linkText("home"));
    }

    private void selectContact(ContactData contact) {
        click(By.cssSelector(String.format("input[value='%s']", contact.id())));
    }

    public List<ContactData> getList() {
        var contacts = new ArrayList<ContactData>();
        var rows = manager.driver.findElements(By.name("entry"));
        for (var row : rows) {
            var cells = row.findElements(By.tagName("td"));
            var lastname = cells.get(1).getText();
            var firstname = cells.get(2).getText();
            var checkbox = row.findElement(By.name("selected[]"));
            String id = checkbox.getAttribute("value");
//            contacts.add(new ContactData(firstname, "", lastname, "", "").withId(id));
        }
        return contacts;
    }

    public String getPhones(ContactData contact) {
        return manager.driver.findElement(By.xpath(
                String.format("//input[@id='%s']/../..td[6]", contact.id()))).getText();
    }

    public Map<String, String> getPhones() {
        var result = new HashMap<String, String>();
        List<WebElement> rows = manager.driver.findElements(By.name("entry"));
        for (WebElement row : rows) {
            var id = row.findElement(By.tagName("input")).getAttribute("id");
            var phones = row.findElements(By.tagName("td")).get(5).getText();
            result.put(id, phones);
        }
        return result;
    }

    public ContactData infoFromEditForm(int id) {
        openEditFormById(id);
        String firstname = manager.driver.findElement(By.name("firstname")).getAttribute("value");
        String lastname = manager.driver.findElement(By.name("lastname")).getAttribute("value");
        String home = manager.driver.findElement(By.name("home")).getAttribute("value");
        String mobile = manager.driver.findElement(By.name("mobile")).getAttribute("value");
        String work = manager.driver.findElement(By.name("work")).getAttribute("value");
        String address = manager.driver.findElement(By.name("address")).getAttribute("value");
        String email = manager.driver.findElement(By.name("email")).getAttribute("value");
        String email2 = manager.driver.findElement(By.name("email2")).getAttribute("value");
        String email3 = manager.driver.findElement(By.name("email3")).getAttribute("value");
        manager.driver.navigate().back();
        return new ContactData()
                .withId(String.valueOf(id))
                .withFirstname(firstname)
                .withLastname(lastname)
                .withHome(home)
                .withMobile(mobile)
                .withWork(work)
                .withSecondary("")
                .withAddress(address)
                .withEmail(email)
                .withEmail2(email2)
                .withEmail3(email3);
    }

    public List<ContactData> all() {
        var contacts = new ArrayList<ContactData>();
        var rows = manager.driver.findElements(By.name("entry"));
        for (var row : rows) {
            var cells = row.findElements(By.tagName("td"));
            var lastname = cells.get(1).getText();
            var firstname = cells.get(2).getText();
            var address = cells.get(3).getText();
            var allEmails = cells.get(4).getText();
            var allPhones = cells.get(5).getText();
            var id = row.findElement(By.name("selected[]")).getAttribute("value");

            // создаём ContactData и кладём дополнительные данные в map, чтобы не ломать record
            var contact = new ContactData(id, firstname, "", lastname, "", "", "", "", "", "",
                    address, "", "", "");

            // добавляем данные в кэш-хранилище для теста (см. ниже)
            cachedPhones.put(id, allPhones);
            cachedEmails.put(id, allEmails);

            contacts.add(contact);
        }
        return contacts;
    }

}