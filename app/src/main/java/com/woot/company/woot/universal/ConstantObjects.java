package com.woot.company.woot.universal;


/**
 * Created by BuAli_bluehorn on 01-Jul-15.
 */
public class ConstantObjects {

    public String hotel_id;
    public String hotel_name;
    public String phone;
    public String address;
    public String old_price;
    public String new_price;
    public String description;
    public String background_image;
    public String icon;
    public String website;
    public String email;
    public String rating;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ConstantObjects(String hotel_id, String hotel_name, String phone, String address, String old_price, String new_price, String description, String background_image, String icon, String website, String email, String rating, String city_id) {
        this.hotel_id = hotel_id;
        this.hotel_name = hotel_name;
        this.phone = phone;
        this.address = address;
        this.old_price = old_price;
        this.new_price = new_price;
        this.description = description;
        this.background_image = background_image;
        this.icon = icon;
        this.website = website;
        this.email = email;
        this.rating = rating;
        this.city_id = city_id;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRatings() {
        return Ratings;
    }

    public void setRatings(String ratings) {
        Ratings = ratings;
    }

    public String city_id;
    public String city_name;
    public String city_image;
    public String number_of_hotels;

    public ConstantObjects(String city_id, String city_name, String city_image, String number_of_hotels, String roomName, String roomDetail) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.city_image = city_image;
        this.number_of_hotels = number_of_hotels;
        RoomName = roomName;
        RoomDetail = roomDetail;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_image() {
        return city_image;
    }

    public void setCity_image(String city_image) {
        this.city_image = city_image;
    }

    public String getNumber_of_hotels() {
        return number_of_hotels;
    }

    public void setNumber_of_hotels(String number_of_hotels) {
        this.number_of_hotels = number_of_hotels;
    }

    public String RoomName;
    public String RoomDetail;
    public String RoomPrice;
    public String RoomPriceOld;
    public String RoomImage;

    public String HotelName;
    public String HotelDescription;
    public String HotelImage;
    public String HotelIcon;
    public String HotelId;




    public String CityName;
    public String CityImage;
    public String CityId;
    public String HotelsNu;

    public String ObjectId;
    public String CategoryId;

    public String UName;
    public String Comments;
    public String Ratings;

//    public ConstantObjects(String uName, String comments, String ratings){
//        this.UName = uName;
//        this.Comments = comments;
//        this.Ratings = ratings;
//    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public ConstantObjects(String hotelId, String hotelName,String address,String roomPrice,
                           String roomPriceOld, String hotelDescription,
                           String hotelImage,String hotelIcon,String rating, String email, String phone,
                           String website){
        this.HotelId = hotelId;
        this.HotelName = hotelName;

        this.RoomPrice = roomPrice;
        this.RoomPriceOld = roomPriceOld;
        this.HotelImage = hotelImage;
        this.HotelIcon = hotelIcon;

        this.HotelDescription = hotelDescription;


    }
    public ConstantObjects(String hotelId, String hotelName, String hotelImage){
        this.HotelId = hotelId;
        this.HotelName = hotelName;
        this.HotelImage = hotelImage;
    }
    public ConstantObjects(String cityId, String cityName, String cityImage, String hotelsNu){
        this.CityId = cityId;
        this.CityName = cityName;
        this.CityImage = cityImage;
        this.HotelsNu = hotelsNu;
    }

    public String getHotelsNu() {
        return HotelsNu;
    }

    public void setHotelsNu(String hotelsNu) {
        HotelsNu = hotelsNu;
    }



    public String getRoomPriceOld() {
        return RoomPriceOld;
    }

    public void setRoomPriceOld(String roomPriceOld) {
        RoomPriceOld = roomPriceOld;
    }

    public String getHotelIcon() {
        return HotelIcon;
    }

    public void setHotelIcon(String hotelIcon) {
        HotelIcon = hotelIcon;
    }






    public String getHotelDescription() {
        return HotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        HotelDescription = hotelDescription;
    }

    public ConstantObjects(String objectId, String roomName, String roomDetail, String roomPrice, String roomImage){
        this.ObjectId=objectId;
        this.RoomName = roomName;
        this.RoomDetail = roomDetail;
        this.RoomPrice = roomPrice;
        this.RoomImage = roomImage;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCityImage() {
        return CityImage;
    }

    public void setCityImage(String cityImage) {
        CityImage = cityImage;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getHotelImage() {
        return HotelImage;
    }

    public void setHotelImage(String hotelImage) {
        HotelImage = hotelImage;
    }

    public String getHotelId() {
        return HotelId;
    }

    public void setHotelId(String hotelId) {
        HotelId = hotelId;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getRoomDetail() {
        return RoomDetail;
    }

    public void setRoomDetail(String roomDetail) {
        RoomDetail = roomDetail;
    }

    public String getRoomPrice() {
        return RoomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        RoomPrice = roomPrice;
    }

    public String getRoomImage() {
        return RoomImage;
    }

    public void setRoomImage(String roomImage) {
        RoomImage = roomImage;
    }

    public ConstantObjects(){

    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getObjectId() {
        return ObjectId;
    }

    public void setObjectId(String objectId) {
        ObjectId = objectId;
    }


}
