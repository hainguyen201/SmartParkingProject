<template>
  <div class="entrance">
    <el-row>
      <el-col :span="10">
        <el-row class="selectcamera">
          <el-col :span="6"><span style="line-height: 40px">Chọn cổng:</span></el-col>
          <el-col :span="8">
            <el-select v-model="value" placeholder="Select" filterable @change="updateCamera($event)">
              <el-option v-for="(item, index) in listGates" :key="index" :label="item.gateName" :value="item.gateId">
              </el-option>
            </el-select>
          </el-col>
        </el-row>
        <el-row class="view">
          <el-col>
            <img :src="imageSrc" ref="cameraVideo" style="height:340px" />
          </el-col>
        </el-row>
        <el-row>
          <el-button @click="controlGate(0)" v-if="value!=null">Đóng cổng</el-button>
          <el-button @click="controlGate(1)" v-if="value!=null">Mở cổng</el-button>
        </el-row>
      </el-col>
      <el-col :span="14">
        <el-row>
          <el-col :span="20">
            Danh sách cổng vào/ra
            <el-table :data='listGatesPage'>
              <el-table-column prop="gateId" label="Mã cổng"></el-table-column>
              <el-table-column prop="gateName" label="Tên cổng"></el-table-column>
              <el-table-column prop="typeName" label="Loại cổng"></el-table-column>
              <el-table-column prop="vehicleTypeName" label="Loại phương tiện" width="140"></el-table-column>
              <el-table-column label="Chức năng">
                <template slot-scope="scope">
                  <el-button @click="openDialogEdit(scope.$index, scope.row)" type="text" size="small">Sửa</el-button>
                  <el-button type="text" size="small" @click="openDeleteDialog(scope.$index, scope.row)">Xóa</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="4">
            <el-button @click="openAddDialog">Thêm</el-button>
          </el-col>
        </el-row>
        <el-row>
          <div class="block">
            <!-- <span class="demonstration">When you have few pages</span> -->
            <el-pagination layout="prev, pager, next" :total="listGates.length" :page-size="10"
              v-if="listGates.length>0" :current-page.sync="currentPage" @current-change="handleCurrentChangePage">
            </el-pagination>
          </div>
        </el-row>

      </el-col>

    </el-row>
    <el-dialog title="Thêm cổng" :visible.sync="dialogAddForm">
      <el-form>

        <el-form-item label="Mã cổng" :label-width="formLabelWidth">
          <el-input v-model="gateAdd.gateId">
          </el-input>
        </el-form-item>
        <el-form-item label="Tên cổng" :label-width="formLabelWidth">
          <el-input v-model="gateAdd.gateName">
          </el-input>
        </el-form-item>
        <el-form-item label="Loại cổng" :label-width="formLabelWidth">
          <el-select v-model="gateAdd.type" clearable placeholder="Select">
            <el-option v-for="item in gateTypeOption" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Loại phương tiện" :label-width="formLabelWidth">
          <el-select v-model="gateAdd.vehicleType" clearable placeholder="Select">
            <el-option v-for="item in vehicleTypeOption" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogAddForm = false">Huỷ</el-button>
        <el-button type="primary" @click="addGate">Xác nhận</el-button>
      </span>
    </el-dialog>
    <el-dialog title="Sửa cổng" :visible.sync="dialogEditForm">
      <el-form>

        <el-form-item label="Mã cổng" :label-width="formLabelWidth">
          <el-input v-model="gateEdit.gateId" disabled>
          </el-input>
        </el-form-item>
        <el-form-item label="Tên cổng" :label-width="formLabelWidth">
          <el-input v-model="gateEdit.gateName">
          </el-input>
        </el-form-item>
        <el-form-item label="Loại cổng" :label-width="formLabelWidth">
          <el-select v-model="gateEdit.type" clearable placeholder="Select">
            <el-option v-for="item in gateTypeOption" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Loại phương tiện" :label-width="formLabelWidth">
          <el-select v-model="gateEdit.vehicleType" clearable placeholder="Select">
            <el-option v-for="item in vehicleTypeOption" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogEditForm = false">Huỷ</el-button>
        <el-button type="primary" @click="editGate">Xác nhận</el-button>
      </span>
    </el-dialog>
    <el-dialog title="Cảnh báo" :visible.sync="dialogDeleteForm" width="30%">
      <span>Xác nhận xóa {{gateDelete.gateName}}?</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogDeleteForm = false">Huỷ</el-button>
        <el-button type="primary" @click="deleteGate">Xác nhận</el-button>
      </span>
    </el-dialog>

    <el-row class="gate"> </el-row>
  </div>
</template>
<style scoped>
  .el-row {
    margin-bottom: 4px;
  }

</style>
<script>
  import {
    mapActions
  } from "Vuex";
  export default {
    name: "Entrance",
    data() {
      return {
        currentPage: 1,
        value: null,
        listGates: [],
        listGatesPage: [],
        imageSrc: null,
        dialogEditForm: false,
        dialogAddForm: false,
        dialogDeleteForm: false,
        formLabelWidth: "200px",
        gateEdit: {
          gateId: null,
          gateName: null,
          type: null,
          vehicleType: null
        },
        gateAdd: {
          gateId: null,
          gateName: null,
          type: null,
          vehicleType: null
        },
        gateDelete: {
          gateId: null,
          gateName: null,
          type: null,
          vehicleType: null
        },
        gateTypeOption: [{
            value: 0,
            label: "Cổng ra"
          },
          {
            value: 1,
            label: "Cổng vào"
          }
        ],
        vehicleTypeOption: [{
            value: 1,
            label: "Xe máy"
          },
          {
            value: 2,
            label: "Ô tô"
          },
        ]
      };
    },

    methods: {
      ...mapActions(['getAllDeviceService', 'getAllGateService', 'updateGateService', 'addGateService',
        'deleteGateService', 'getDeviceByGateService', 'controlGateService'
      ]),
      handleCurrentChangePage(val) {
        this.currentPage = val
        console.log(this.currentPage)
        var len = this.listGates.length;
        var end;
        if (len < val * 10)
          end = len;
        else
          end = val * 10;
        this.listGatesPage = this.listGates.slice((val - 1) * 10, end);
      },
      updateCamera(e) {
        console.log(e);
      },

      checkValidImageSrc(ip) {
        if (ip !== null && ip !== "http://localhost:8080/") return true;
        return false;
      },
      getGateTypeName(gateType) {
        if (gateType == 0) {
          return "Cổng ra";
        } else if (gateType == 1)
          return "Cổng vào";
        else
          return gateType;
      },
      getGateVehicleType(vehicleType) {
        if (vehicleType == 1) {
          return "Xe máy";
        } else if (vehicleType == 2) {
          return "Ô tô";
        } else return vehicleType
      },
      openDialogEdit(index, row) {
        this.dialogEditForm = true
        index = (this.currentPage - 1) * 10 + index
        this.gateEdit.gateId = this.listGates[index].gateId;
        this.gateEdit.gateName = this.listGates[index].gateName;
        this.gateEdit.type = this.listGates[index].type;
        this.gateEdit.vehicleType = this.listGates[index].vehicleType;
      },
      openDeleteDialog(index, row) {
        this.dialogDeleteForm = true
        this.gateDelete.gateId = this.listGates[index].gateId;
        this.gateDelete.gateName = this.listGates[index].gateName;
      },
      openAddDialog(index, row) {
        this.dialogAddForm = true;

      },
      addGate() {
        var isExist=0;
        this.getAllGateService().then(data => {
          data.forEach(gateElement => {
            if (gateElement.gateId == this.gateAdd.gateId) {
              this.$message.error('Mã cổng tồn tại');
              isExist=1;
              return 0;
            }
          })
          if(!isExist)
            this.addGateService(this.gateAdd).then(data => {
              this.dialogAddForm = false;
              this.reload();
            })

        })

      },
      editGate() {
        this.updateGateService(this.gateEdit).then(data => {
          this.dialogEditForm = false;
          this.reload();
        })
      },
      deleteGate() {
        this.deleteGateService(this.gateDelete.gateId).then(data => {
          this.dialogDeleteForm = false;
          this.reload();
        })
      },
      controlGate(commandType) {
        var command = {
          gateId: this.value,
          commandType: commandType
        }
        console.log(command)
        this.controlGateService(command).then(data => {
          console.log("control gate success")
        })
      },
      reload() {
        this.listGates = []
        this.getAllGateService().then(data => {
          const gateLen = data.length
          if (gateLen <= 0)
            return;
          data.forEach(gateElement => {

            this.getDeviceByGateService(gateElement.gateId).then(devices => {
              gateElement.cameraAddr = ''
              gateElement.typeName = this.getGateTypeName(gateElement.type)
              gateElement.vehicleTypeName = this.getGateVehicleType(gateElement.vehicleType);
              devices.forEach(device => {
                if (device.type == 3) {
                  gateElement.cameraAddr = device.address;
                }
              })
              this.listGates.push(gateElement);
              if (this.listGates.length == gateLen) {
                this.listGates.sort((a, b) => a.gateId - b.gateId)
                this.handleCurrentChangePage(this.currentPage);
              }

            })
          })
        })
      }




    },
    mounted: function () {
      // this.captureImage();
    },
    watch: {
      value: function () {
        this.imageSrc = this.listGates.filter(g => g.gateId == this.value)[0].cameraAddr
      }
    },
    created() {
      this.reload();

    }
  };

</script>
