import moment from "moment";
const actions={
    async dateFormat({commit}, value) {
        if (value != null) {
          return await moment(String(value)).format("yyyy-MM-DD HH:mm:ss");
        } else {
          return value;
        }
      },
    dateFormatEdit({commit}, value){
        if (value != null) {
            return moment(String(value)).format("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        } else {
            return value;
        }
    },
}
export default{
    actions
}