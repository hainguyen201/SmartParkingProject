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
        <el-col :span="5">
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
        <el-col :span="3">
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
        <el-col :span="3">
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
        <el-dialog title="Thêm thiết bị " :visible.sync="dialogAddVehicle">
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
            <el-form-item label="Ảnh xe vào" :label-width="formLabelWidth">
              <el-input v-model="vehicleAdd.entranceImage" type="file" ></el-input>
            </el-form-item>
            <el-form-item label="Ảnh xe ra" :label-width="formLabelWidth">
              <el-input v-model="vehicleAdd.exitImage" type="file" ></el-input>
            </el-form-item>
            <el-form-item label="Thời gian vào" :label-width="formLabelWidth">
              <el-date-picker type="datetime" v-model="vehicleAdd.entranceTime" ></el-date-picker>
            </el-form-item>
            <el-form-item label="Thời gian ra" :label-width="formLabelWidth">
              <el-date-picker type="datetime" v-model="vehicleAdd.exitTime" ></el-date-picker>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogAddVehicle = false">Hủy</el-button>
            <el-button type="primary" @click="addVehicle">Xác nhận</el-button>
          </span>
        </el-dialog>
      </el-row>
    </div>
    <div class="table-info" style="width: 100%">
      <el-table :data="listVehicles" style="width: 100%">
        <el-table-column fixed prop="id" label="ID" width="40">
        </el-table-column>
        <el-table-column prop="licenseNumber" label="Biển số" width="140">
        </el-table-column>
        <el-table-column prop="type" label="Loại phương tiện" width="165">
        </el-table-column>
        <el-table-column prop="entranceTime" label="Thời gian vào" width="120">
        </el-table-column>
        <el-table-column prop="exitTime" label="Thời gian ra" width="120">
        </el-table-column>
        
        <el-table-column width="220">
          <template slot-scope="scope">
            <el-button @click="openDialogDetail(scope.$index, scope.row)" type="text" size="small">Chi tiết</el-button>
            <el-button @click="openDialogClone(scope.$index, scope.row)" type="text" size="small">Sao chép</el-button>
            <el-button @click="openDialogEdit(scope.$index, scope.row)" type="text" size="small">Sửa</el-button>
            <el-button type="text" size="small" @click="openDeleteDialog(scope.$index, scope.row)">Xóa</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :title="dialogEditTitle " :visible.sync="dialogEditForm">
        <el-form :model="deviceEdit">
          <el-form-item label="Tên Thiết bị" :label-width="formLabelWidth">
            <el-input v-model="deviceEdit.name"></el-input>
          </el-form-item>
          <el-form-item label="Địa chỉ" :label-width="formLabelWidth">
            <el-input v-model="deviceEdit.address"></el-input>
          </el-form-item>
          <el-form-item label="Loại Thiết bị" :label-width="formLabelWidth">
            <el-select v-model="deviceEdit.type" clearable placeholder="Select">
              <el-option v-for="item in vehicleTypeOption" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Trạng thái" :label-width="formLabelWidth">
            <el-select v-model="deviceEdit.status" clearable placeholder="Select">
              <el-option v-for="item in deviceStatusOption" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Mã vị trí" :label-width="formLabelWidth">
            <el-input v-model="deviceEdit.positionId" type="number"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogEditForm = false">Huỷ</el-button>
          <el-button type="primary" @click="editDevice">Xác nhận</el-button>
        </span>
      </el-dialog>
      <el-dialog title="Cảnh báo" :visible.sync="dialogDeleteVisible" width="30%">
        <span>Xác nhận xóa thiết bị {{deviceDelete.name}}?</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">Huỷ</el-button>
          <el-button type="primary" @click="deleteDevice">Xác nhận</el-button>
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
        vehicleSearch: {
          address: '',
          id: null,
          name: '',
          positionId: null,
          type: null,
          status: null
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
        deviceEdit: {
          address: '',
          id: null,
          name: '',
          positionId: null,
          type: null,
          createdDate: null,
          modifiedDate: null,
          status: null
        },
        vehicleAdd: {
          address: '',
          name: '',
          positionId: null,
          type: null,
          createdDate: null,
          modifiedDate: null,
          status: null
        },
        deviceDelete: {
          address: '',
          name: '',
          positionId: null,
          type: null,
          createdDate: null,
          modifiedDate: null,
          status: null
        }
      };
    },
    methods: {
      ...mapActions(["getAllVehicleService", "updateDeviceService", "addVehicleService", "deleteDeviceService",
        "searchDeviesService"
      ]),
      openDialogEdit(index, row) {
        this.dialogEditForm = true;
        this.dialogEditTitle = "Sửa thiết bị";
        this.editType = "edit";
        this.deviceEdit.id = row.id;
        this.deviceEdit.name = row.name;
        this.deviceEdit.address = row.address;
        this.deviceEdit.type = row.type;
        this.deviceEdit.positionId = row.positionId;
        this.deviceEdit.status = row.status;
      },
      openDialogClone(index, row) {
        this.dialogEditForm = true;
        this.dialogEditTitle = "Sao chép thiết bị";
        this.editType = "copy";
        this.deviceEdit.name = row.name;
        this.deviceEdit.address = row.address;
        this.deviceEdit.type = row.type;
        this.deviceEdit.positionId = row.positionId;
        this.deviceEdit.status = row.status;
      },
      openDialogDetail(index, row){

      },
      editDevice() {
        this.deviceEdit.status = this.getDeviceStatusCode(this.deviceEdit.status);
        this.deviceEdit.type = this.getDeviceTypeCode(this.deviceEdit.type);
        this.deviceEdit.createdDate = null;
        this.deviceEdit.modifiedDate = null;
        if (this.editType === "edit") {
          this.updateDeviceService(this.deviceEdit).then(data => {
            this.dialogEditForm = false
            this.reload();
          });
        } else if (this.editType === "copy") {
          this.deviceEdit.id = null;
          this.addVehicleService(this.deviceEdit).then(data => {
            this.dialogEditForm = false
            this.reload();
          })
        }

      },
      openDeleteDialog(index, row) {
        this.dialogDeleteVisible = true
        this.deviceDelete = row
      },
      deleteDevice() {
        this.deleteDeviceService(this.deviceDelete.id).then(data => {
          this.dialogDeleteVisible = false;
          this.reload();
        })
      },
      addVehicle() {
        this.vehicleAdd.status = 0;
        this.addVehicleService(this.vehicleAdd).then(data => {
          this.dialogAddVehicle = false;
          this.reload();
        })
      },
      searchData() {
        console.log(this.vehicleSearch);
        this.searchDeviesService(JSON.stringify(this.vehicleSearch)).then(data => {
          this.listVehicles = [];
          this.handleData(data);
        })
      },
      dateFormat(value) {
        if (value) {
          return moment(String(value)).format("DD/MM/yyyy");
        } else {
          return "";
        }
      },
      getVehicleTypeLabel(type) {
        if (type == 1) {
          return "Xe máy";
        } else if (type == 2) {
          return "ô tô";
        }
      },
      getDeviceTypeCode(type) {
        if (type == "Cảm biến khoảng cách") {
          return 1;
        } else if (type == "Servo thanh chắn") {
          return 2;
        } else if (type == "Camera") {
          return 3;
        } else
          return type
      },
      getDeviceStatusLabel(status) {
        if (status == 1) {
          return "Hoạt động";
        } else if (status == 0) {
          return "Không hoạt động";
        }
      },
      getDeviceStatusCode(status) {
        if (status === "Hoạt động")
          return 1;
        else if (status === "Không hoạt động")
          return 0;
        else
          return status;
      },
      reload() {
        this.listVehicles = [];
        this.getAllVehicleService().then(data => {
          this.handleData(data);
        });
      },
      handleData(data) {
        data.forEach((element) => {
          element.entranceTime = this.dateFormat(element.entranceTime);
          element.exitTime = this.dateFormat(element.exitTime);
          element.type = this.getVehicleTypeLabel(element.type);
          element.status = this.getDeviceStatusLabel(element.status);
          this.listVehicles.push(element);
        });
      },
      resetSearchInput() {
        this.vehicleSearch.address = "";
        this.vehicleSearch.id = "";
        this.vehicleSearch.status = null;
        this.vehicleSearch.positionId = "";
        this.vehicleSearch.type = null
        this.vehicleSearch.name = "";
      }
    },
    created() {
      this.reload();
    },
  };

</script>
