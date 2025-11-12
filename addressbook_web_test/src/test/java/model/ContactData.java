package model;

public record ContactData(String id,
                          String firstname,
                          String middlename,
                          String lastname,
                          String nickname,
                          String photo,
                          String home,
                          String mobile,
                          String work,
                          String secondary,
                          String address,
                          String email,
                          String email2,
                          String email3
) {

//    public ContactData {
//        if (id == null) id = "";
//    }

    public ContactData() {this("","", "", "", "", "", "", "", "", "", "", "", "", "");}

//    public ContactData(String firstname, String middlename, String lastname, String nickname, String photo) {
//        this("", firstname, middlename, lastname, nickname, photo);
//    }

    public ContactData withId(String id) {
        return new ContactData(id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, this.work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withLastname(String lastname) {
        return new ContactData(this.id, this.firstname, this.middlename, lastname, this.nickname, this.photo, this.home, this.mobile, this.work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withFirstname(String firstname) {
        return new ContactData(this.id, firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, this.work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withMiddlename(String middlename) {
        return new ContactData(this.id, this.firstname, middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, this.work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withNickname(String nickname) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, nickname, this.photo, this.home, this.mobile, this.work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withPhoto(String photo) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname , photo, this.home, this.mobile, this.work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withHome(String home) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, home, this.mobile, this.work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withMobile(String mobile) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, mobile, this.work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withWork(String work) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, work, this.secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withSecondary(String secondary) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, this.work, secondary, this.address, this.email, this.email2, this.email3);
    }

    public ContactData withAddress(String address) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, this.work, secondary, address, this.email, this.email2, this.email3);
    }
    public ContactData withEmail(String email) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, this.work, secondary, this.address, email, this.email2, this.email3);
    }
    public ContactData withEmail2(String email2) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, this.work, secondary, this.address, this.email, email2, this.email3);
    }
    public ContactData withEmail3(String email3) {
        return new ContactData(this.id, this.firstname, this.middlename, this.lastname, this.nickname, this.photo, this.home, this.mobile, this.work, secondary, this.address, this.email, this.email2, email3);
    }

}
