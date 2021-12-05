const baseUrl="http://localhost:8089"
const modelUrl="http://localhost:8787"
const configJsonHeader={
    headers: {
        'Content-Type': 'application/json'
    }
}
const configFormDataHeader={
    headers: {
        'Content-Type': 'multipart/form-data'
    }
}
const modelUrlCaptureImage=modelUrl+"/vehicle_images"
const baseUrlDevice=baseUrl+"/devices"
const baseUrlUser=baseUrl+"/users"
const baseUrlModel=baseUrl+"/models"
const baseUrlVehicle= baseUrl+"/vehicles"
const baseUrlParkingSlot=baseUrl+"/parking_slots"
const baseUrlParkingArea=baseUrl+"/parking_areas"
function handleResponse(respone){
    if(respone.status===200){
        return respone.data;
    }else{
        console.log("Lỗi dữ liệu trên server")
        return null;
    }
}
export default{
    baseUrlModel,
    baseUrlUser,
    baseUrlDevice,
    baseUrlVehicle,
    baseUrlParkingArea,
    baseUrlParkingSlot,
    configJsonHeader,
    configFormDataHeader,
    modelUrlCaptureImage,
    handleResponse
}