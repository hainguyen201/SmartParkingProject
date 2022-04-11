const baseUrl="http://localhost:8089"
const modelUrl="http://localhost:8787"
// const configJsonHeader={
//     headers: {
//         'Content-Type': 'application/json'
//     }
// }

// const configFormDataHeader={
//     headers: {
//         'Content-Type': 'multipart/form-data',
//         'Authorization': localStorage.getItem('token')
//     }
// }
const modelUrlCaptureImage=modelUrl+"/vehicle_images"
const baseUrlDevice=baseUrl+"/devices"
const baseUrlUser=baseUrl+"/users"
const baseUrlGate=baseUrl+"/gates"
const baseUrlModel=baseUrl+"/models"
const baseUrlVehicle= baseUrl+"/vehicles"
const baseUrlParkingSlot=baseUrl+"/parking_slots"
const baseUrlParkingArea=baseUrl+"/parking_areas"
const baseUrlAuth=baseUrl+"/authenticate"
const baseUrlRegister= baseUrl+ "/register"
const baseJsonHeader={
    headers: {
        'Content-Type': 'application/json'
    }
}
function handleResponse(respone){
    if(respone.status===200){
        return respone.data;
    }else{
        console.log("Lỗi dữ liệu trên server")
        return null;
    }
}
function configJsonHeader(){
    const header={
        headers: {
            'Content-Type': 'application/json',
            'Authorization': localStorage.getItem('token')
        }
    }
    return header;
}
function configFormDataHeader(){
    const header={
        headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': localStorage.getItem('token')
        }
    }
    return header
}
export default{
    baseUrlModel,
    baseUrlUser,
    baseUrlDevice,
    baseUrlVehicle,
    baseUrlParkingArea,
    baseUrlParkingSlot,
    baseUrlAuth,
    baseUrlGate,
    configJsonHeader,
    configFormDataHeader,
    modelUrlCaptureImage,
    handleResponse,
    baseUrlRegister,
    baseJsonHeader
    
}