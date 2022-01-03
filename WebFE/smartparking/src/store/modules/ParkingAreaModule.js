import axios from 'axios'
import BaseModel from './BaseModel';
const actions={
    async getParkingAreaService({commit}){
        console.log(BaseModel.configJsonHeader())
        const response = await axios.get(BaseModel.baseUrlParkingArea, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    },
    async getParkingSlotsByIdService({commit}, id){
        const response =await axios.get(BaseModel.baseUrlParkingArea+"/slot/"+id, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    },
    async getEmptySlotByParkingAreaId({commit}, id){
        const response = await axios.get(BaseModel.baseUrlParkingArea+"/empty-slot/"+id, BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    }

}
export default {
    actions
}