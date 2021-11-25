<template>
  <div class="model">
    <el-row>
      <el-col :span="12">
        <el-row :gutter="8">
          <el-col :span="8">
            <el-select v-model="value" placeholder="Select" filterable @change="updateModelView($event)">
              <el-option
                v-for="item in models"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-button @click="dialogAddForm = true"> Thêm mô hình</el-button>
          </el-col>
          <el-dialog title="Thêm mô hình " :visible.sync="dialogAddForm">
            <el-form :model="form">
              <el-form-item
                label="Tên mô hình"
                :label-width="formLabelWidth">
                <el-input v-model="modelAdd.name" ></el-input>
              </el-form-item>
              <el-form-item label="Url" :label-width="formLabelWidth">
                <el-input v-model="modelAdd.url" ></el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button @click="dialogAddForm = false">Cancel</el-button>
              <el-button type="primary" @click="addNewModel">Confirm</el-button>
            </span>
          </el-dialog>
        </el-row>
        <h4 class="model-title">Thông tin mô hình dự đoán biển số xe</h4>
        <el-row>
          <el-col :span="6">Tên mô hình</el-col>
          <el-col :span="6">{{ modelView.name }}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6">Url</el-col>
          <el-col :span="6">{{ modelView.url }}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6">Ngày tạo</el-col>
          <el-col :span="6">{{ dateFormat(modelView.createdDate) }}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6">Ngày cập nhật</el-col>
          <el-col :span="6">{{ dateFormat(modelView.modifiedDate) }}</el-col>
        </el-row>
        <el-row>
          <el-col :span="6"><el-button @click="openEditDialog">Sửa mô hình</el-button></el-col>
          <el-dialog title="Sửa mô hình " :visible.sync="dialogEditForm">
            <el-form :model="form">
              <el-form-item
                label="Tên mô hình"
                :label-width="formLabelWidth">
                <el-input v-model="modelEdit.name" ></el-input>
              </el-form-item>
              <el-form-item label="Url" :label-width="formLabelWidth">
                <el-input v-model="modelEdit.url" ></el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button @click="dialogEditForm = false">Cancel</el-button>
              <el-button type="primary" @click="updateModel">Confirm</el-button>
            </span>
          </el-dialog>
          <el-col :span="8">
            <el-button @click="dialogDeleteVisible = true">Xóa mô hình</el-button>
            <el-dialog
              title="Cảnh báo"
              :visible.sync="dialogDeleteVisible"
              width="30%"
            >
              <span>Xác nhận xóa mô hình {{modelView.name}}?</span>
              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogDeleteVisible = false">Cancel</el-button>
                <el-button type="primary" @click="handleDeleteModel"
                  >Confirm</el-button
                >
              </span>
            </el-dialog>
          </el-col>
          <el-col :span="6"
            ><span>Sử dụng</span> <el-switch v-model="isActive" @change="activeModel($event)"> </el-switch
          ></el-col>
        </el-row>
        <el-row :gutter="4" style="margin-top: 100px">
          <!-- <el-col :span="8">
            <el-popover placement="top" width="160" v-model="isDeleteModel">
              <p>Are you sure to delete this?</p>
              <div style="text-align: right; margin: 0">
                <el-button
                  size="mini"
                  type="text"
                  @click="isDeleteModel = false"
                  >cancel</el-button
                >
                <el-button
                  type="primary"
                  size="mini"
                  @click="isDeleteModel = false"
                  >confirm</el-button
                >
              </div>

              <el-button slot="reference">Xóa mô hình</el-button>
            </el-popover>
          </el-col> -->
          <!-- <el-col :span="3" ><el-button type="text">Kích hoạt</el-button></el-col> -->
        </el-row>
      </el-col>
      <el-col :span="12">
        <el-row>
          <el-col :span="12"
            ><div>
              <img :src="previewImage" class="uploading-image" />
              <input
                style="width: 150px"
                type="file"
                accept="image/jpeg"
                @change="uploadImage"
              /></div
          ></el-col>
          <el-col :span="12">
            <img :src="licenseImage" class="uploading-image" />

            <div style="margin-top: 4px">Biển số xe</div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 32px">
          <el-col>
            <h4 class="margin-basic">
              Kết quả: <span>{{ predictResult }}</span>
            </h4>
          </el-col>
          <el-col style="margin-top: 32px">
            <el-button @click="runModel">Chạy mô hình</el-button>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>
<style scoped>
.margin-basic {
  margin-top: 8px;
  margin-bottom: 8px;
}
.model {
  margin-left: 24px;
}
.model-title {
  margin-top: 32px;
  margin-bottom: 24px;
}

.el-row {
  margin-top: 8px;
  margin-bottom: 24px;
}
.hideUpload > div {
  display: none;
}
.uploading-image {
  display: flex;
  height: 200px;
  width: 90%;
}
</style>
<script>
import { mapActions, mapGetters } from "Vuex";
import moment from 'moment'
import axios from 'axios'
import BaseModel from '../store/modules/BaseModel'
export default {
  name: "Model",
  data() {
    return {
      options: [
        {
          value: "Option1",
          label: "Option1",
        },
        {
          value: "Option2",
          label: "Option2",
        },
      ],
      dialogDeleteVisible: false,
      dialogAddForm: false,
      dialogEditForm: false,
      form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
      },
      models: [],
      modelView:{
        id:'',
        name: '',
        url: '',
        status: '',
        createdDate: '',
        modifiedDate: ''
      },
      modelAdd:{
        name: '',
        url: '',
        status: ''
      },
      modelEdit:{
        id:'',
        name: '',
        url: '',
        status: '',
        createdDate: '',
        modifiedDate: ''
      },
      formLabelWidth: '120px',
      value: "",
      modelName: "Mô hình 1",
      modelAPI: "http://192.168.1.12/api/v1/model1",
      modelCreatedDate: "20/10/2021",
      isActive: false,

      previewImage: null,
      predictResult: "",
      isDeleteModel: false,
      licenseImage: "",
    };
  },
  methods: {
    ...mapActions(["getLicense", "getAllModel", "addModel", "getModelById", "deleteModel", "updateModelService"]),
    
    uploadImage(e) {
      const image = e.target.files[0];
      const reader = new FileReader();
      reader.readAsDataURL(image);
      this.predictResult = "";
      reader.onload = (e) => {
        this.previewImage = e.target.result;
        this.licenseImage = "";
      };
    },
    async runModel() {
      let fd = new FormData();
      
      fd.append("image", this.previewImage);
      console.log("before: ", fd)
      const response = await axios.post("http://localhost:8787/license", fd, BaseModel.configFormDataHeader);
      this.licenseImage = "data:image/jpeg;base64," + response.data.license;
      this.predictResult = response.data.code;
      // this.getLicense(this.modelView.url, fd).then((data) => {
      //   this.licenseImage = "data:image/jpeg;base64," + data.license;
      //   this.predictResult = data.code;
      // });
    },
    addNewModel(){
      this.modelAdd.status=0
      console.log(JSON.stringify(this.modelAdd))
      this.addModel(JSON.stringify(this.modelAdd)).then(data=>{
        this.dialogAddForm=false;
        this.reload();
      })
      // console.log(JSON.stringify(this.modelAdd))
    },
    updateModelView(index){
      this.models.forEach(element => {
        if(element.id==index){
          this.modelView=element;
          this.isActive=element.status==1?true:false
        }
      });
    },
    handleDeleteModel(){
      this.deleteModel(this.modelView.id).then(data=>{
        console.log(data);
        this.dialogDeleteVisible=false;
        this.reload();
      })
    },
    reload(){
      this.models=[];
      this.getAllModel().then(lstModel=>{
      lstModel.forEach(element => {
        // element.createdDate=this.dateFormat(element.createdDate)
        // element.modifiedDate=this.dateFormat(element.modifiedDate)
        this.models.push(element);
      });
      this.models=lstModel;
    });
    },
    openEditDialog(){
      this.dialogEditForm = true;
      this.modelEdit=this.modelView
    },
    updateModel(){
      this.updateModelService(this.modelEdit).then(data=>{
        
        console.log(data);
        this.dialogEditForm=false;
        this.reload()
      })
    },
    activeModel(e){
      if(e==true){

      }
      console.log(e)
      
    },
    dateFormat(value) {
      if (value) {
        return moment(String(value)).format("DD/MM/yyyy");
      } else {
        return "";
      }
    },
    loadData(){
      this.reload();
      this.models.forEach(element => {
        console.log(element)
        if(element.status==1){
        this.value=element.id
      }
      
    });
    }
    
  },
  created() {
    this.loadData();    
  },
};
</script>
