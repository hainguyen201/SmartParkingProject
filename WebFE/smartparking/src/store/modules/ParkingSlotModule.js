import axios from 'axios';
import BaseModel from "./BaseModel"
const actions={
    async getAllParkingSlot({commit}, id){
        const response= await axios.get(BaseModel.baseUrlParkingSlot, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    },
    async deleteParkingSlotFromAreaService({commit}, parkingSlotId){
        const response = await axios.delete(BaseModel.baseUrlParkingSlot+'/'+parkingSlotId, BaseModel.configJsonHeader() )
        return BaseModel.handleResponse(response);
    },
    async addParkingSlotService({commit}, parkingSlot){
        const response= await axios.post(BaseModel.baseUrlParkingSlot, parkingSlot, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    }
}
export default{
    actions
}