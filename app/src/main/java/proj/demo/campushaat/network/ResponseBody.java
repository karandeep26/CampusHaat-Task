package proj.demo.campushaat.network;

import java.io.Serializable;

/**
 * Created by stpl on 5/23/2017.
 */

public class ResponseBody implements Serializable {
    Address address;
    BaseResponse baseResponse;

    public Address getAddress() {
        return address;
    }

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    static public class Address implements Serializable{
        String addressId,zipCode;
        String city,country,lattitude,locality,longitude,room,state;

        public String getAddressId() {
            return addressId;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public String getLattitude() {
            return lattitude;
        }

        public String getLocality() {
            return locality;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getRoom() {
            return room;
        }

        public String getState() {
            return state;
        }

        public String getZipCode() {
            return zipCode;
        }
    }
    static public class BaseResponse implements Serializable{
        String message,statusCode;

        public String getMessage() {
            return message;
        }

        public String getStatusCode() {
            return statusCode;
        }
    }

}
