package phonebook.spring;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name = "address")
public class AddressModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Door number is required")
    private String doorNo;

    @NotNull(message = "Street is required")
    private String street;

    private String area;

    @NotNull(message = "District is required")
    private String district;

    @NotNull(message = "State is required")
    private String state;

    @NotNull(message = "Pin code is required")
    @Pattern(regexp = "[0-9]+", message = "Must be valid pin code")
    private String pinCode;

    @OneToOne
    @JoinColumn(name = "person_id")
    private PersonModel person;

    public PersonModel getPerson() {
        return person;
    }

    public void setPerson(PersonModel person) {
        this.person = person;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

}
