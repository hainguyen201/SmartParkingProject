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
    },
    async searchParkingAreaService({commit}, parkingArea){
        const response = await axios.post(BaseModel.baseUrlParkingArea+'/search', JSON.stringify(parkingArea), BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response);
    },
    async editParkingAreaService({commit}, parkingArea){
        const response = await axios.put(BaseModel.baseUrlParkingArea+'/'+parkingArea.id, JSON.stringify(parkingArea), BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response);
    },
    async addParkingAreaService({commit}, parkingArea){
        const response = await axios.post(BaseModel.baseUrlParkingArea, JSON.stringify(parkingArea), BaseModel.configJsonHeader());
        return BaseModel.handleResponse(response);
    },
    async deleteParkingAreaService({commit}, id){
        const response = await axios.delete(BaseModel.baseUrlParkingArea+'/'+id, BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response);
    },
    async searchParkingAreaByParkingSlot({commit}, id){
        const response =await axios.get(BaseModel.baseUrlParkingArea+'/search/parkingslot/'+id, BaseModel.configJsonHeader())
        return BaseModel.handleResponse(response);
    }

}
export default {
    actions
}