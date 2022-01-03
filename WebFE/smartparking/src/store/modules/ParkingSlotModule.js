import axios from 'axios';
import BaseModel from "./BaseModel"
const actions={
    async getAllParkingSlot({commit}, id){
        const response= await axios.get(BaseModel.baseUrlParkingSlot, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    }
}
export default{
    actions
}