import axios from 'axios'
import BaseModel from './BaseModel';
const actions = {
    async loginService({commit}, user){
        try{
            const configJsonHeader={
                headers: {
                    'Content-Type': 'application/json'
                }
            }
            const response=await axios.post(BaseModel.baseUrlAuth, user, configJsonHeader)
            return response;
        }catch(error){
            console.error(error);
            return 'error';
        }
    },
    async registerService({commit}, user){
        const respone= await axios.post(BaseModel.baseUrlRegister,JSON.stringify(user), BaseModel.baseJsonHeader)
        if(respone.status==200){
            // this.$message({
            //     message: 'Bạn đã đăng ký thành công',
            //     type: 'success'
            //   });
            return respone.data;
        }else {
            // this.$message.error('Lỗi khi đăng ký');
              return null
        }
    }
}

export default {
    actions
};