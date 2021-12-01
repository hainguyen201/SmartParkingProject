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
const baseUrlVehicle= baseUrl+"/vehicle"
export default{
    baseUrlModel,
    baseUrlUser,
    baseUrlDevice,
    baseUrlVehicle,
    configJsonHeader,
    configFormDataHeader,
    modelUrlCaptureImage
}