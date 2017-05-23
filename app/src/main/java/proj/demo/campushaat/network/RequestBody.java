package proj.demo.campushaat.network;

/**
 * Created by stpl on 5/23/2017.
 */

public class RequestBody {
    private String addressId;
    private String room;
    private String locality;
    private String zipCode;
    private String city;
    private String state;
    private String country;
    private String longitude;
    private String lattitude;

    public RequestBody(String addressId, String room, String locality, String zipCode, String
            city, String state, String country, String longitude, String lattitude) {
        this.addressId = addressId;
        this.room = room;
        this.locality = locality;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getRoom() {
        return room;
    }

    public String getLocality() {
        return locality;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLattitude() {
        return lattitude;
    }
}
