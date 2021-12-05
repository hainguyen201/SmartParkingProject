import moment from "moment";
function dateFormat(value){
    if (value) {
        return moment(String(value)).format("DD/MM/yyyy");
      } else {
        return "";
      }  
}
export default{
    dateFormat
}