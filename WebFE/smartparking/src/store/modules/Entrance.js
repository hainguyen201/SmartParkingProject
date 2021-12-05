import axios from 'axios'
import moment from 'moment'
import BaseModel from './BaseModel';
const actions={
    async captureImageService({commit}, cameraIP){
        const respone=await axios.post(BaseModel.modelUrlCaptureImage, JSON.stringify(cameraIP), BaseModel.configJsonHeader)
        return respone.data
    }
}
export default{
    actions
}