<template>
  <div style="margin-left: 16px">
    <div class="search">
      <el-row :gutter="20">
        <el-col :span="4">
          <span>Biển số :</span>
        </el-col>
        <el-col :span="6">
          <el-input v-model="vehicleSearch.licenseNumber"></el-input>
        </el-col>
        <el-col :span="4">
          <span type="text">Loại phương tiện:</span>
        </el-col>
        <el-col :span="6">
          <el-select v-model="vehicleSearch.type" placeholder="Select" clearable>
            <el-option v-for="item in vehicleTypeOption" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-col>

      </el-row>
      <el-row :gutter="20">
        <el-col :span="4">
          <span>Thời gian vào từ: </span>
        </el-col>
        <el-col :span="6">
          <el-date-picker type="datetime" v-model="vehicleSearch.entranceTimeStart"></el-date-picker>
        </el-col>
        <el-col :span="4">
          <span type="text">Đến</span>
        </el-col>
        <el-col :span="6">
          <el-date-picker v-model="vehicleSearch.entranceTimeEnd" type="datetime"></el-date-picker>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="4">
          <span>Thời gian ra từ: </span>
        </el-col>
        <el-col :span="6">
          <el-date-picker type="datetime" v-model="vehicleSearch.exitTimeStart"></el-date-picker>
        </el-col>
        <el-col :span="4">
          <span type="text">Đến</span>
        </el-col>
        <el-col :span="6">
          <el-date-picker type="datetime" v-model="vehicleSearch.exitTimeEnd"></el-date-picker>
        </el-col>
      </el-row>
      <el-row :gutter="4" type="flex" justify="start">
        <el-col :span="3">
          <el-button type="primary" plain round @click="searchData"><i class="el-icon-search"></i> Tìm kiếm
          </el-button>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" plain round @click="resetSearchInput">Thiết lập lại</el-button>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" plain round @click="dialogAddVehicle = true;"><i class="el-icon-plus"></i> Thêm mới
          </el-button>
        </el-col>
        <el-dialog title="Thêm phương tiện " :visible.sync="dialogAddVehicle">
          <el-form :model="vehicleAdd">
            <el-form-item label="Biển số" :label-width="formLabelWidth">
              <el-input v-model="vehicleAdd.licenseNumber"></el-input>
            </el-form-item>
            <el-form-item label="Loại phương tiện" :label-width="formLabelWidth">
              <el-select v-model="vehicleAdd.type" placeholder="Select" clearable>
                <el-option v-for="item in vehicleTypeOption" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="Thời gian vào" :label-width="formLabelWidth">
              <el-date-picker type="datetime" v-model="vehicleAdd.entranceTime">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="Thời gian ra" :label-width="formLabelWidth">
              <el-date-picker type="datetime" v-model="vehicleAdd.exitTime">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="Ảnh xe vào" :label-width="formLabelWidth">
              <input type="file" :v-model="vehicleAdd.entranceImage" @change="previewAddEntranceImage"
                ref="addEntranceImage">
              <el-button title="Loại bỏ file" icon="el-icon-circle-close" circle @click="clearImage('addEntrance')">
              </el-button>
              <div>
                <img :src="previewAddEntranceSrc" alt="" class="preview-image">
              </div>
            </el-form-item>
            <el-form-item label="Ảnh xe ra" :label-width="formLabelWidth">
              <input :v-model="vehicleAdd.exitImage" type="file" @change="previewAddExitImage" ref="addExitImage">
              <el-button title="Loại bỏ file" icon="el-icon-circle-close" circle @click="clearImage('addExit')">
              </el-button>
              <div>
                <img :src="previewAddExitSrc" alt="" class="preview-image">
              </div>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogAddVehicle = false">Hủy</el-button>
            <el-button type="primary" @click="addVehicle">Xác nhận</el-button>
          </span>
        </el-dialog>
      </el-row>
    </div>
    <div>
      Tổng số xe trong bến: {{numberInParking}}
    </div>
    <div class="table-info" style="width: 100%">
      <el-table :data="listVehiclesPage" style="width: 100%">
        <el-table-column fixed prop="id" label="ID" width="40">
        </el-table-column>
        <el-table-column prop="licenseNumber" label="Biển số" width="140">
        </el-table-column>
        <el-table-column prop="type" label="Loại phương tiện" width="165">
        </el-table-column>
        <el-table-column prop="entranceTime" label="Thời gian vào" width="160">
        </el-table-column>
        <el-table-column prop="exitTime" label="Thời gian ra" width="160">
        </el-table-column>
        <el-table-column prop="entranceImage" label="Ảnh xe vào" width="160">
          <template slot-scope="scope">
            <img :src="scope.row.entranceImage" alt="" style="height: 40px">
          </template>
        </el-table-column>
        <el-table-column prop="exitImage" label="Ảnh xe ra" width="160">
          <template slot-scope="scope">
            <img :src="scope.row.exitImage" alt="" style="height: 40px">
          </template>
        </el-table-column>

        <el-table-column width="220">
          <template slot-scope="scope">
            <!-- <el-button @click="openDialogDetail(scope.$index, scope.row)" type="text" size="small">Chi tiết</el-button> -->
            <!-- <el-button @click="openDialogClone(scope.$index, scope.row)" type="text" size="small">Sao chép</el-button> -->
            <el-button @click="openDialogEdit(scope.$index, scope.row)" type="text" size="small">Sửa</el-button>
            <el-button type="text" size="small" @click="openDeleteDialog(scope.$index, scope.row)">Xóa</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="block">
        <!-- <span class="demonstration">When you have few pages</span> -->
        <el-pagination layout="prev, pager, next" :total="listVehicles.length" :page-size="10" v-if="listVehicles.length>0"
          @current-change="handleCurrentChangePage">
        </el-pagination>
      </div>
      <el-dialog title="Sửa phương tiện" :visible.sync="dialogEditForm">
        <el-form :model="vehicleEdit">
          <el-form-item label="Biển số" :label-width="formLabelWidth">
            <el-input v-model="vehicleEdit.licenseNumber"></el-input>
          </el-form-item>
          <el-form-item label="Loại phương tiện" :label-width="formLabelWidth">
            <el-select v-model="vehicleEdit.type" placeholder="Select" clearable>
              <el-option v-for="item in vehicleTypeOption" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="Thời gian vào" :label-width="formLabelWidth">
            <el-date-picker type="datetime" v-model="vehicleEdit.entranceTime"></el-date-picker>
          </el-form-item>
          <el-form-item label="Thời gian ra" :label-width="formLabelWidth">
            <el-date-picker type="datetime" v-model="vehicleEdit.exitTime">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="Ảnh xe vào" :label-width="formLabelWidth">
            <input :v-model="vehicleEdit.entranceImage" type="file" @change="previewEditEntranceImage"
              ref="editEntranceImage">
            <el-button title="Loại bỏ file" icon="el-icon-circle-close" circle @click="clearImage('editEntrance')">
            </el-button>
            <div>
              <img :src="previewEditEntranceSrc" alt="" class="preview-image">
            </div>
          </el-form-item>
          <el-form-item label="Ảnh xe ra" :label-width="formLabelWidth">
            <input :v-model="vehicleEdit.exitImage" type="file" @change="previewEditExitImage" ref="editExitImage">
            <el-button title="Loại bỏ file" icon="el-icon-circle-close" circle @click="clearImage('editExit')">
            </el-button>
            <div>
              <img :src="previewEditExitSrc" alt="" class="preview-image">
            </div>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogEditForm = false">Huỷ</el-button>
          <el-button type="primary" @click="editVehicle">Xác nhận</el-button>
        </span>
      </el-dialog>
      <el-dialog title="Cảnh báo" :visible.sync="dialogDeleteVisible" width="30%">
        <span>Xác nhận xóa phương tiện {{vehicleDelete.type}} có biển số {{vehicleDelete.licenseNumber}}?</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">Huỷ</el-button>
          <el-button type="primary" @click="deleteVehicle">Xác nhận</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>
<style scoped>
  .search {
    height: 10%vh;
  }

  .table-info {
    height: 90%vh;
  }

  .el-row {
    margin-bottom: 20px;
  }

  .el-col>span {
    line-height: 40px;
    font-size: 16px;
  }

  .preview-image {
    height: 150px;
    width: 150px;
    margin-top: 8px;
  }

  .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 100%;
  }

  .el-select {
    width: 100%;
  }

</style>
<script>
  import {
    mapActions
  } from "Vuex";
  import moment from "moment";
  export default {
    name: "Vehicle",
    data() {
      return {
        numberInParking: 1,
        vehicleSearch: {
          id: null,
          licenseNumber: null,
          type: null,
          entranceTimeEnd: null,
          entranceTimeStart: null,
          exitTimeEnd: null,
          exitTimeStart: null,
        },
        address: "",
        positionId: "",
        deviceStatus: -1,
        listVehicles: [],
        dialogEditForm: false,
        dialogAddVehicle: false,
        formLabelWidth: "200px",
        dialogDeleteVisible: false,
        dialogEditTitle: '',
        editType: "",
        deviceStatusOption: [{
            value: 0,
            label: "Không hoạt động",
          },
          {
            value: 1,
            label: "Hoạt động",
          },
        ],
        vehicleTypeOption: [{
            value: 1,
            label: "Xe máy",
          },
          {
            value: 2,
            label: "Ô tô",
          },
        ],
        vehicleEdit: {
          id: null,
          licenseNumber: null,
          type: null,
          entranceTime: null,
          exitTime: null,
          entranceImage: null,
          exitImage: null,
        },
        vehicleAdd: {
          id: null,
          licenseNumber: null,
          type: null,
          entranceTime: null,
          exitTime: null,
          entranceImage: null,
          exitImage: null,
        },
        vehicleDelete: {
          id: null,
          licenseNumber: null,
          type: null,
          entranceTime: null,
          exitTime: null,
          entranceImage: null,
          exitImage: null,
        },
        // vehicleEditFile: {
        //   entranceImage: null,
        //   exitImage: null
        // },
        imageAddreview: [],
        previewAddEntranceSrc: null,
        previewAddExitSrc: null,
        previewEditEntranceSrc: null,
        previewEditExitSrc: null,
        listVehiclesPage: []
      };
    },
    methods: {
      ...mapActions(["getAllVehicleService", "editVehicleService", "addVehicleService", "deleteVehicleService",
        "searchVehicleService", 'getNumVehicleInsideService'
      ]),
      handleCurrentChangePage(val) {
        var len = this.listVehicles.length;
        var end;
        if (len < val * 10)
          end = len;
        else
          end = val * 10;


        this.listVehiclesPage = this.listVehicles.slice((val - 1) * 10, end);

      },
      clearImage(type) {
        if (type === "addEntrance") {
          this.$refs.addEntranceImage.value = null;
          this.previewAddEntranceSrc = null;
        }
        if (type === "addExit") {
          this.$refs.addExitImage.value = null;
          this.previewAddExitSrc = null;
        }
        if (type === "editEntrance") {
          this.$refs.editEntranceImage.value = null;
          this.previewEditEntranceSrc = null;
        }
        if (type === "editExit") {
          this.$refs.editExitImage.value = null;
          this.previewEditExitSrc = null;
        }

      },

      handleImage(e) {
        // let imagePre= imagePreview
        const image = e.target.files[0];
        const reader = new FileReader();
        reader.readAsDataURL(image);
        return reader;
      },
      previewAddEntranceImage(e) {
        const reader = this.handleImage(e);
        reader.onload = (e) => {
          this.previewAddEntranceSrc = e.target.result;
        };
      },
      previewAddExitImage(e) {
        const reader = this.handleImage(e);
        reader.onload = (e) => {
          this.previewAddExitSrc = e.target.result;
        };
      },
      previewEditEntranceImage(e) {
        const reader = this.handleImage(e);
        reader.onload = (e) => {
          this.previewEditEntranceSrc = e.target.result;
        };
      },
      previewEditExitImage(e) {
        const reader = this.handleImage(e);
        reader.onload = (e) => {
          this.previewEditExitSrc = e.target.result;
        };
      },
      openDialogEdit(index, row) {
        this.dialogEditForm = true;
        this.dialogEditTitle = "Sửa phương tiện";
        this.editType = "edit";
        this.vehicleEdit.id = row.id;
        this.vehicleEdit.licenseNumber = row.licenseNumber;
        this.vehicleEdit.type = this.getVehicleTypeCode(row.type);
        this.vehicleEdit.entranceTime = row.entranceTime;
        this.vehicleEdit.exitTime = row.exitTime;
        this.previewEditEntranceSrc = row.entranceImage;
        this.previewEditExitSrc = row.exitImage;
      },
      openDialogDetail(index, row) {

      },

      openDeleteDialog(index, row) {
        this.dialogDeleteVisible = true
        this.vehicleDelete = row
      },
      deleteVehicle() {
        this.deleteVehicleService(this.vehicleDelete.id).then(data => {
          this.dialogDeleteVisible = false;
          this.reload();
        })
      },
      editVehicle() {
        let vehicle = {};
        vehicle.licenseNumber = this.vehicleEdit.licenseNumber;
        vehicle.type = this.vehicleEdit.type;
        vehicle.entranceTime = this.vehicleEdit.entranceTime == null ? null : new Date(this.vehicleEdit.entranceTime);
        vehicle.exitTime = this.vehicleEdit.exitTime == null ? null : new Date(this.vehicleEdit.exitTime);
        let fd = new FormData();
        fd.append("id", this.vehicleEdit.id);
        fd.append("vehicle", JSON.stringify(vehicle))


        if (this.previewEditExitSrc == null) {
          fd.append("exitImageDelete", true);
        } else {
          if (this.$refs.editExitImage.files.length > 0) {
            fd.append("exitImage", this.$refs.editExitImage.files[0]);
          }
        }
        if (this.previewEditEntranceSrc == null) {
          fd.append("entranceImageDelete", true);
        } else {
          if (this.$refs.editEntranceImage.files.length > 0) {
            fd.append("entranceImage", this.$refs.editEntranceImage.files[0]);
          }
        }
        this.editVehicleService(fd).then(data => {
          console.log(data);
          this.dialogEditForm = false;
          this.reload();
        })

      },
      addVehicle() {
        let vehicle = {};
        vehicle.licenseNumber = this.vehicleAdd.licenseNumber;
        vehicle.type = this.vehicleAdd.type;
        vehicle.entranceTime = this.vehicleAdd.entranceTime;
        vehicle.exitTime = this.vehicleAdd.exitTime
        let fd = new FormData();
        fd.append("vehicle", JSON.stringify(vehicle))
        if (this.$refs.addEntranceImage.files.length > 0) {
          fd.append("entranceImage", this.$refs.addEntranceImage.files[0]);
        }
        if (this.$refs.addExitImage.files.length > 0) {
          fd.append("exitImage", this.$refs.addExitImage.files[0]);
        }
        this.addVehicleService(fd).then(data => {
          console.log(data);
          this.dialogAddVehicle = false;
          this.reload();
        })
      },
      searchData() {
        console.log(this.vehicleSearch);
        let fd = new FormData();
        let vehicle = {};
        vehicle.licenseNumber = this.vehicleSearch.licenseNumber;
        vehicle.type = this.vehicleSearch.type;
        fd.append("vehicle", JSON.stringify(vehicle))
        fd.append("fromEtrance", this.dateFormat(this.vehicleSearch.entranceTimeStart));
        fd.append("toEntrance", this.dateFormat(this.vehicleSearch.entranceTimeEnd));
        fd.append("fromExit", this.dateFormat(this.vehicleSearch.exitTimeStart));
        fd.append("toExit", this.dateFormat(this.vehicleSearch.exitTimeEnd));
        console.log(fd.get('fromEtrance'));
        this.searchVehicleService(fd).then(data => {
          this.listVehicles = [];
          this.handleData(data);
        })
      },
      dateFormat(value) {
        if (value != null) {
          return moment(String(value)).format("yyyy-MM-DD HH:mm:ss");
        } else {
          return value;
        }
      },
      dateFormatEdit(value) {
        if (value != null) {
          return moment(String(value)).format("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        } else {
          return value;
        }
      },
      getVehicleTypeLabel(type) {
        if (type == 1) {
          return "Xe máy";
        } else if (type == 2) {
          return "ô tô";
        } else
          return type;
      },
      getVehicleTypeCode(type) {
        if (type == "Xe máy") {
          return 1;
        } else if (type == "ô tô") {
          return 2;
        } else
          return type;
      },



      reload() {
        this.listVehicles = [];
        this.numberInParking=0;
        this.getAllVehicleService().then(data => {
          this.handleData(data);
        });
        this.getNumVehicleInsideService().then(data=>{
          this.numberInParking=data;
        })
        
      },
      handleData(data) {
        // this.numberInParking = 0;
        data.forEach((element) => {
          element.entranceTime = this.dateFormat(element.entranceTime);
          element.exitTime = this.dateFormat(element.exitTime);
          element.type = this.getVehicleTypeLabel(element.type);
          element.entranceImage = "data:image/jpeg;base64," + element.entranceImage;
          element.exitImage = "data:image/jpeg;base64," + element.exitImage;
          this.listVehicles.push(element);
          // if (element.exitTime == "" || element.exitTime == null)
          //   this.numberInParking++;
        });
        if (this.listVehicles.length > 10)
          this.listVehiclesPage = this.listVehicles.slice(0, 10);
        else {
          this.listVehiclesPage = this.listVehicles.slice(0, this.listVehicles.length);
        }
      },
      resetSearchInput() {
        this.vehicleSearch.licenseNumber = "";
        this.vehicleSearch.type = null;
        this.vehicleSearch.entranceTimeEnd = null;
        this.vehicleSearch.entranceTimeStart = null;
        this.vehicleSearch.exitTimeStart = null
        this.vehicleSearch.exitTimeEnd = null;
      }
    },
    created() {
      this.reload();
    },
  };

</script>
