<template>
  <div style="margin-left: 16px">
    <div class="search">
      <el-row :gutter="20">
        <!-- <el-col :span="3">
          <span>Tên thiết bị :</span>
        </el-col>
        <el-col :span="4">
          <el-input v-model="deviceSearch.name"></el-input>
        </el-col> -->
        <el-col :span="3">
          <span type="text">Loại thiết bị:</span>
        </el-col>
        <el-col :span="5">
          <el-select v-model="deviceSearch.type" placeholder="Select" clearable>
            <el-option v-for="item in deviceTypeOption" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <span type="text">Trạng thái:</span>
        </el-col>
        <el-col :span="5">
          <el-select v-model="deviceSearch.status" clearable placeholder="Select">
            <el-option v-for="item in deviceStatusOption" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <span type="text">Mã cổng :</span>
        </el-col>
        <el-col :span="5">
          <el-input v-model="deviceSearch.gateId" type="number"></el-input>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="3">
          <span>Địa chỉ: </span>
        </el-col>
        <el-col :span="5">
          <el-input v-model="deviceSearch.address"></el-input>
        </el-col>
        <el-col :span="3">
          <span type="text">Mã vị trí :</span>
        </el-col>
        <el-col :span="5">
          <el-input v-model="deviceSearch.positionId" type="number"></el-input>
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
          <el-button type="primary" plain round @click="dialogAddForm = true;"><i class="el-icon-plus"></i> Thêm mới
          </el-button>
        </el-col>
        <el-dialog title="Thêm thiết bị " :visible.sync="dialogAddForm">
          <el-form :model="deviceAdd">
            <!-- <el-form-item label="Tên Thiết bị" :label-width="formLabelWidth">
              <el-input v-model="deviceAdd.name"></el-input>
            </el-form-item> -->
            <el-form-item label="Loại Thiết bị" :label-width="formLabelWidth">
              <el-select v-model="deviceAdd.type" clearable placeholder="Select">
                <el-option v-for="item in deviceTypeOption" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="Địa chỉ" :label-width="formLabelWidth" v-if="deviceAdd.type==3">
              <el-input v-model="deviceAdd.address"></el-input>
            </el-form-item>

            <el-form-item label="Mã vị trí" :label-width="formLabelWidth" v-if="deviceAdd.type==11">
              <el-input v-model="deviceAdd.positionId" type="number"></el-input>
            </el-form-item>
            <el-form-item label="Cổng" :label-width="formLabelWidth" v-if="deviceAdd.type!=11">
              <el-select v-model="deviceAdd.gateId" clearable placeholder="Select">
                <el-option v-for="item in listGate" :key="item.gateId" :label="item.gateId" :value="item.gateId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogAddForm = false">Hủy</el-button>
            <el-button type="primary" @click="addDevice">Xác nhận</el-button>
          </span>
        </el-dialog>
      </el-row>
    </div>
    <div class="table-info" style="width: 100%">
      <el-table :data="listDevicesPage" style="width: 100%">
        <el-table-column fixed prop="id" label="ID" width="40">
        </el-table-column>
        <!-- <el-table-column prop="name" label="Tên thiết bị" width="140">
        </el-table-column> -->
        <el-table-column prop="typeName" label="Loại thiết bị" width="180">
        </el-table-column>
        <el-table-column prop="statusName" label="Trạng thái" width="100">
        </el-table-column>
        <el-table-column prop="createdDate" label="Ngày tạo" width="100">
        </el-table-column>
        <el-table-column prop="address" label="Địa chỉ" width="180">
        </el-table-column>
        <el-table-column prop="positionId" label="Mã vị trí" width="100">
        </el-table-column>
        <el-table-column prop="gateId" label="Mã cổng" width="100">
        </el-table-column>
        <el-table-column width="150">
          <template slot-scope="scope">
            <!-- <el-button @click="openDialogClone(scope.$index, scope.row)" type="text" size="small">Sao chép</el-button> -->
            <el-button @click="openDialogEdit(scope.$index, scope.row)" type="text" size="small">Sửa</el-button>
            <el-button type="text" size="small" @click="openDeleteDialog(scope.$index, scope.row)">Xóa</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="block">
            <!-- <span class="demonstration">When you have few pages</span> -->
            <el-pagination layout="prev, pager, next" :total="listDevices.length" :page-size="10" v-if="listDevices.length>0"
              @current-change="handleCurrentChangePage">
            </el-pagination>
          </div>
      <el-dialog :title="dialogEditTitle " :visible.sync="dialogEditForm">
        <el-form>
          <!-- <el-form-item label="Tên Thiết bị" :label-width="formLabelWidth">
            <el-input v-model="deviceEdit.name"></el-input>
          </el-form-item> -->

          <el-form-item label="Loại Thiết bị" :label-width="formLabelWidth">
            <el-select v-model="deviceEdit.type" clearable placeholder="Select">
              <el-option v-for="item in deviceTypeOption" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="Trạng thái" :label-width="formLabelWidth">
            <el-select v-model="deviceEdit.status" clearable placeholder="Select">
              <el-option v-for="item in deviceStatusOption" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Địa chỉ" :label-width="formLabelWidth" v-if="deviceEdit.type==3">
            <el-input v-model="deviceEdit.address"></el-input>
          </el-form-item>
          <el-form-item label="Mã vị trí" :label-width="formLabelWidth" v-if="deviceEdit.type==11">
            <el-input v-model="deviceEdit.positionId" type="number"></el-input>
          </el-form-item>
          <el-form-item label="Mã cổng" :label-width="formLabelWidth" v-if="deviceEdit.type!=11">
            <el-select v-model="deviceEdit.gateId" clearable placeholder="Select">
              <el-option v-for="item in listGate" :key="item.gateId" :label="item.gateId" :value="item.gateId">
              </el-option>
            </el-select>
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

  .el-select {
    width: 100%
  }

</style>
<script>
  import {
    mapActions
  } from "Vuex";
  import moment from "moment";
  export default {
    name: "Device",
    data() {
      return {
        deviceSearch: {
          address: '',
          id: null,
          name: '',
          positionId: null,
          type: null,
          status: null,
          gateId: null,
        },
        address: "",
        positionId: "",
        deviceStatus: -1,
        listDevices: [],
        listDevicesPage:[],
        dialogEditForm: false,
        dialogAddForm: false,
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
        deviceTypeOption: [{
            value: 11,
            label: "Cảm biến vị trí đỗ",
          },
          {
            value: 12,
            label: "Cảm biến nhận dạng xe",
          },
          {
            value: 13,
            label: "Cảm biến đóng cổng",
          },
          {
            value: 2,
            label: "Servo thanh chắn",
          },
          {
            value: 3,
            label: "Camera",
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
          status: null,
          gateId: null,
        },
        deviceAdd: {
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
        },
        listGate: []
      };
    },
    methods: {
      ...mapActions(["getAllDeviceService", "updateDeviceService", "addDeviceService", "deleteDeviceService",
        "searchDeviesService", "getAllGateService"
      ]),
      handleCurrentChangePage(val){
        var len = this.listDevices.length;
        var end;
        if (len < val * 10)
          end = len;
        else
          end = val * 10;
        this.listDevicesPage = this.listDevices.slice((val - 1) * 10, end);
      },
      openDialogEdit(index, row) {
        this.dialogEditForm = true;
        this.dialogEditTitle = "Sửa thiết bị";
        this.editType = "edit";
        this.deviceEdit.id = row.id;
        this.deviceEdit.name = row.name;
        this.deviceEdit.address = row.address;
        this.deviceEdit.type = this.listDevices[index].type;
        this.deviceEdit.positionId = row.positionId;
        this.deviceEdit.status = row.status;
        this.deviceEdit.gateId = this.listDevices[index].gateId;
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
      editDevice() {
        // this.deviceEdit.status = this.getDeviceStatusCode(this.deviceEdit.status);
        // this.deviceEdit.type = this.getDeviceTypeCode(this.deviceEdit.type);
        this.deviceEdit.createdDate = null;
        this.deviceEdit.modifiedDate = null;
        if (this.editType === "edit") {
          //Nếu là vị trí đỗ 
          if (this.deviceEdit.type == 11) {
            this.deviceEdit.address = null
            this.deviceEdit.gateId = null
          } else {
            this.deviceEdit.positionId = null;
            if (this.deviceEdit.type != 3)
              this.deviceEdit.address = null;

          }
          this.updateDeviceService(this.deviceEdit).then(data => {
            this.dialogEditForm = false
            this.searchData();
          });
        } else if (this.editType === "copy") {
          this.deviceEdit.id = null;
          this.addDeviceService(this.deviceEdit).then(data => {
            this.dialogEditForm = false
            this.searchData();
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
          this.searchData();
        })
      },
      addDevice() {
        this.deviceAdd.status = 0;
        if (this.deviceAdd.type == 11) {
          this.deviceAdd.address = null
          this.deviceAdd.gateId = null
        } else {
          this.deviceAdd.positionId = null;
          if (this.deviceAdd.type != 3)
            this.deviceAdd.address = null;

        }
        this.addDeviceService(this.deviceAdd).then(data => {
          this.dialogAddForm = false;
          this.searchData();
        })
      },
      searchData() {
        console.log(this.deviceSearch);
        this.searchDeviesService(JSON.stringify(this.deviceSearch)).then(data => {
          this.listDevices.length=0;
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
      getDeviceTypeLabel(type) {
        if (type == 11) {
          return "Cảm biến vị trí đỗ";
        } else if (type == 12) {
          return "Cảm biến nhận dạng xe";
        } else if (type == 13) {
          return "Cảm biến đóng cổng";
        } else if (type == 2) {
          return "Servo thanh chắn";
        } else if (type == 3) {
          return "Camera";
        }
      },
      getDeviceTypeCode(type) {
        if (type == "Cảm biến vị trí đỗ") {
          return 11;
        } else if (type == "Cảm biến nhận dạng xe") {
          return 12;
        } else if (type == "Cảm biến đóng cổng") {
          return 13;
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
        this.getAllDeviceService().then((data) => {
          this.handleData(data);
        });
      },
      handleData(data) {
        this.listGate = []
        this.listDevices=[]
        this.listDevicesPage=[]
        const listDeviceLen=data.length;
        if(listDeviceLen<=0){
          return;
        }
        this.getAllGateService().then(gateData => {
          this.listGate = gateData
          data.forEach((element) => {
            element.createdDate = this.dateFormat(element.createdDate);
            element.typeName = this.getDeviceTypeLabel(element.type);
            element.statusName = this.getDeviceStatusLabel(element.status);
            gateData.forEach((gateElement) => {
              if (element.gateId === gateElement.gateId) {
                element.gateName = gateElement.gateName;
              }
            })
            this.listDevices.push(element);
            if(listDeviceLen==this.listDevices.length){
              this.handleCurrentChangePage(1)
            }
          });
        })

      },
      resetSearchInput() {
        this.deviceSearch.address = "";
        this.deviceSearch.id = "";
        this.deviceSearch.status = null;
        this.deviceSearch.positionId = "";
        this.deviceSearch.type = null
        this.deviceSearch.name = "";
        this.deviceSearch.gateId=null;
      }
    },
    created() {
      this.reload();
    },
  };

</script>
