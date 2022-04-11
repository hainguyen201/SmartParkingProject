<template>
  <div style="font-size: 16px">
    <el-row class="searchParking" :gutter="10">
      <el-col :span="3" class="center"><span class="search-label">Tên bãi đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập tên bãi đỗ" v-model="parkingAreaSearch.name"></el-input>
      </el-col>
      <el-col :span="3" class="center"><span class="search-label">Mã bãi đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập mã bãi đỗ" v-model="parkingAreaSearch.id"></el-input>
      </el-col>
      <el-col :span="3" class="center"><span class="search-label">Mã vị trí đỗ: </span>
      </el-col>
      <el-col :span="4">
        <el-input placeholder="Nhập mã vị trí đỗ" v-model="parkingAreaSearch.parkingSlotId"></el-input>
      </el-col>
    </el-row>

    <el-row class="buttonSearch" justify="center" style="margin-top: 16px">
      <el-button type="primary" plain round @click="searchData">
        <i class="el-icon-search"></i> Tìm kiếm</el-button>
      <el-button type="primary" plain round @click="resetInput">
        <i></i> Thiết lập lại</el-button>
      <el-button type="primary" plain round @click="addParkingAreaDialog=true">
        <i class="el-icon-plus"></i> Thêm bãi đỗ</el-button>
    </el-row>
    <el-dialog title="Thêm bãi đỗ" :visible.sync="addParkingAreaDialog">
      <el-form :model="parkingAreaAdd">
        <el-form-item label="Mã bãi đỗ" :label-width="formLabelWidth">
          <el-input v-model="parkingAreaAdd.id"></el-input>
        </el-form-item>
        <el-form-item label="Tên bãi đỗ" :label-width="formLabelWidth">
          <el-input v-model="parkingAreaAdd.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addParkingAreaDialog = false">Đóng</el-button>
        <el-button @click="addParkingArea">Thêm</el-button>
      </span>
    </el-dialog>


    <el-dialog :visible.sync="parkingAreaDetail" width="80%">
      <el-carousel height="400px" :autoplay="false">
        <el-carousel-item v-for="(item, index1) in viewNumber" :key="index1">
          <el-row style="padding-top:16px">
            <el-col :span="2" v-for="(parkingCol, index2) in item" :key="index2"
              v-bind:class="{ active: true, 'col-margin': (index2) % 2, 'first-margin': index2%8==0 }">

              <el-card :body-style="{ padding: '4px' }" v-for="(parkingslot, index3) in parkingCol" :key="index3"
                style="text-align: center">
                <el-tooltip class="item" effect="dark" :content="''+parkingslot.id" placement="top">
                  <img src="../assets/car.png" alt="" v-if="parkingslot.status==1" />
                  <el-button v-if="parkingslot.status==0" style="border:none" type="text">{{parkingslot.id}}</el-button>
                </el-tooltip>
              </el-card>
            </el-col>
          </el-row>

        </el-carousel-item>
      </el-carousel>
      <span slot="footer" class="dialog-footer">
        <el-button @click="reloadParkingView">Tải lại</el-button>
        <el-button @click="parkingAreaDetail = false">Đóng</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="parkingEditDialog" width="80%" title="Sửa bãi đỗ">
      <el-row type="flex" justify="space-between">
        <el-col :span="6" class="parking-area-info">
          <el-form :model="parkingAreaEdit">
            <el-form-item label="Mã bãi đỗ" :label-width="formLabelWidth">
              <el-input v-model="parkingAreaEdit.id"></el-input>
            </el-form-item>
            <el-form-item label="Tên bãi đỗ" :label-width="formLabelWidth">
              <el-input v-model="parkingAreaEdit.name"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="8" class="parking-slot-info">
          <h5>Danh sách vị trí đỗ</h5>
          <el-table :data="listParkingSlotPage">
            <el-table-column fixed prop="id" label="Mã vị trí" width="150">
            </el-table-column>
            <el-table-column prop="status" label="Trạng thái" width="160">
            </el-table-column>
            <!-- <el-table-column prop="type" label="Loại bãi đỗ" width="300">
      </el-table-column> -->
            <el-table-column label="Thao tác" width="120">
              <template slot-scope="scope">
                <el-button @click="openDeleteParkingSlot(scope.$index, scope.row)" type="text" size="small">Xóa
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="block">
            <!-- <span class="demonstration">When you have few pages</span> -->
            <el-pagination layout="prev, pager, next" :total="listParkingSlot.length" :page-size="10"
              @current-change="handleCurrentChangeSlot">
            </el-pagination>
          </div>
        </el-col>
        <el-col :span='4'>
          <el-button @click="addParkingSlotDialog=true">Thêm</el-button>
        </el-col>
        <el-dialog :visible.sync="addParkingSlotDialog" width="20%" append-to-body title="Thêm vị trí đỗ">
          <el-form :model="parkingSlotAdd">
            <el-form-item label="Mã vị trí">
              <el-input v-model="parkingSlotAdd.id"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="addParkingSlot">Thêm</el-button>
            <el-button @click="addParkingSlotDialog = false">Đóng</el-button>
          </span>
        </el-dialog>
        <el-dialog :visible.sync="deleteParkigSlotDialog" width="20%" append-to-body title="Cảnh báo">
          <div>Xác nhận xóa vị trí đỗ {{parkingSlotDeleteId}} trong bãi</div>
          <span slot="footer" class="dialog-footer">
            <el-button @click="deleteParkingSlot()">Xóa</el-button>
            <el-button @click="deleteParkigSlotDialog = false">Đóng</el-button>
          </span>
        </el-dialog>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editParkingArea">Lưu</el-button>
        <el-button @click="parkingEditDialog = false">Đóng</el-button>
      </span>
    </el-dialog>

    <el-table :data="listParkingAreaPage" style="width: 100%; height: 620px; margin-left: 16px; margin-top:16px"
      max-height="620" v-loading="loadingArea">
      <el-table-column fixed prop="id" label="Mã bãi đỗ" width="150">
      </el-table-column>
      <el-table-column prop="name" label="Tên bãi đỗ" width="200">
      </el-table-column>
      <el-table-column prop="numNotInUsed" label="Chỗ chưa đỗ" width="120">
      </el-table-column>
      <el-table-column prop="maxNumber" label="Số lượng tối đa" width="140">
      </el-table-column>
      <el-table-column prop="createdDate" label="Ngày tạo" width="180">
      </el-table-column>
      <!-- <el-table-column prop="type" label="Loại bãi đỗ" width="300">
      </el-table-column> -->
      <el-table-column label="Thao tác" width="160">
        <template slot-scope="scope">
          <el-button @click="showParkingDetail(scope.$index, scope.row)" type="text" size="small">Chi tiết</el-button>
          <el-button type="text" size="small" @click="openDialogEditParkingArea(scope.$index, scope.row)">Sửa
          </el-button>
          <el-button type="text" size="small" @click="openDialogDeleteParkingArea(scope.$index, scope.row)">Xóa
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-row class="pagination">
      <el-pagination layout="prev, pager, next" :total="listParkingArea.length" :page-size="10"
        @current-change="handleCurrentChangeArea">
      </el-pagination>
    </el-row>
    <el-dialog title="Cảnh báo" :visible.sync='deleteParkingAreaDialog' width="20%">
      <div>Xác nhận xóa bãi đỗ {{parkingAreaDelete.name}}?</div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteParkingArea">Xóa</el-button>
        <el-button @click="deleteParkingAreaDialog = false">Đóng</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<style scoped>
  .search-label {
    line-height: 40px;
  }

  .el-card {
    height: 40px;
    margin: 4px;
    border: 1px solid rgb(15, 114, 226);
    border-radius: 10px;
  }

  img {
    object-fit: cover;
    width: 75px;
    height: 30px;
  }

  .col-margin {
    margin-right: 64px;
  }

  .first-margin {
    margin-left: 128px;
  }

  .el-carousel__item:nth-child(n) {
    background-color: #9bacc4;
    border-radius: 5px;
  }

</style>
<script>
  import {
    mapActions
  } from "Vuex";
  import moment from 'moment';
  export default {
    name: "Parking",
    data() {
      return {
        parkingType: null,
        parkingName: "",
        parkingCode: "",
        parkingAreaDetail: false,
        parkingEditDialog: false,
        parkingAreaViewIndex: null,
        formLabelWidth: '120px',
        listParkingSlot: [],
        listParkingSlotPage: [],
        deleteParkigSlotDialog: false,
        addParkingAreaDialog: false,
        deleteParkingAreaDialog: false,
        parkingAreaAdd: {
          id: null,
          name: null
        },
        parkingAreaEdit: {
          id: null,
          name: null,
          parkingSlots: []
        },
        parkingAreaDelete: {
          id: null,
          name: null,
          parkingSlots: []
        },
        parkingAreaSearch: {
          id: null,
          name: null,
          parkingSlotId: null,
        },
        parkingArea: [

        ],
        listParkingArea: [],
        listParkingAreaPage: [],
        viewNumber: [],
        listParkingAreaLength: 0,
        loadingArea: false,
        parkingSlotDeleteId: 0,
        addParkingSlotDialog: false,

        parkingSlotAdd: {
          id: null
        },
        currentPage: 1,


      };
    },
    methods: {
      ...mapActions(['getParkingAreaService', 'getParkingSlotsByIdService', 'getEmptySlotByParkingAreaId',
        'getAllParkingSlot', 'deleteParkingSlotFromAreaService', 'addParkingSlotService',
        'searchParkingAreaService', 'editParkingAreaService', 'addParkingAreaService', 'deleteParkingAreaService',
        'searchParkingAreaByParkingSlot'
      ]),
      openDialogDeleteParkingArea(index, row) {
        this.deleteParkingAreaDialog = true
        this.parkingAreaDelete = this.listParkingAreaPage[index];
      },
      deleteParkingArea() {
        if (this.parkingAreaDelete.parkingSlots.length > 0) {
          this.$message.error("Bãi đỗ vẫn còn vị trí đỗ. Không thể xóa!")
        } else {
          this.deleteParkingAreaService(this.parkingAreaDelete.id).then(data => {
            this.reloadParkingArea();
            this.deleteParkingAreaDialog = false;
          })
        }
      },
      addParkingArea() {
        var isExist = 0;
        this.getParkingAreaService().then(data => {
          data.forEach(element => {
            if (element.id == this.parkingAreaAdd.id) {
              isExist = 1;
              this.$message.error("Mã bãi đỗ đã tồn tại");
              return;
            }
          })
          if (!isExist) {
            this.addParkingAreaService(this.parkingAreaAdd).then(data => {
              this.addParkingAreaDialog = false;
              this.reloadParkingArea();
            })
          }
        })
      },
      editParkingArea() {
        const parkingAreaEditData = {
          id: this.parkingAreaEdit.id,
          name: this.parkingAreaEdit.name
        }
        this.editParkingAreaService(parkingAreaEditData).then(data => {
          this.parkingEditDialog = false
          this.reloadParkingArea()
        })
      },
      resetInput() {
        this.parkingAreaSearch.name = null
        this.parkingAreaSearch.id = null
        this.parkingAreaSearch.parkingSlotId=null;
      },
      searchData() {
        if (this.parkingAreaSearch.parkingSlotId != null && this.parkingAreaSearch.parkingSlotId != '') {
          this.searchParkingAreaByParkingSlot(this.parkingAreaSearch.parkingSlotId).then(data=>{
            this.handleParkingAreaData(data);
          })
        } else {
          this.searchParkingAreaService(this.parkingAreaSearch).then(data => {
            this.handleParkingAreaData(data);
          })
        }

      },
      addParkingSlot() {
        var isExist = 0;
        this.getAllParkingSlot().then(data => {
          data.forEach(element => {
            if (element.id == this.parkingSlotAdd.id) {
              this.$message.error("Mã vị trí đỗ đã tồn tại")
              isExist = 1;
              return;
            }
          })
          if (!isExist) {
            this.parkingSlotAdd.parkingAreaId = this.parkingAreaEdit.id;
            this.addParkingSlotService(this.parkingSlotAdd).then(data => {
              this.reloadParkingSlot();
            })
          }
        })


      },
      openDeleteParkingSlot(index, row) {
        this.deleteParkigSlotDialog = true;
        this.parkingSlotDeleteId = row.id
      },
      deleteParkingSlot() {
        this.deleteParkingSlotFromAreaService(this.parkingSlotDeleteId).then(data => {
          this.reloadParkingSlot();
        })
      },
      reloadParkingSlot() {
        this.getParkingSlotsByIdService(this.parkingAreaEdit.id).then(data3 => {
          this.listParkingArea[this.parkingAreaViewIndex].parkingSlots = data3;
          this.listParkingArea[this.parkingAreaViewIndex].maxNumber = data3.length;
          this.listParkingSlot = data3;
          this.handleCurrentChangeSlot(1)
          // if (this.listParkingSlot.length > 10)
          //   this.listParkingSlotPage = this.listParkingSlot.slice(0, 10);
          // else {
          //   this.listParkingSlotPage = this.listParkingSlot.slice(0, this.listParkingSlot.length);
          // }
          this.deleteParkigSlotDialog = false;
          this.addParkingSlotDialog = false;

        })
      },
      handleCurrentChangeSlot(val) {
        // console.log(this.listParkingSlot);
        var len = this.listParkingSlot.length;
        var end;
        if (len < val * 10)
          end = len;
        else
          end = val * 10;
        this.listParkingSlotPage = this.listParkingSlot.slice((val - 1) * 10, end);
      },
      handleCurrentChangeArea(val) {
        this.currentPage = val
        var len = this.listParkingArea.length;
        var end;
        if (len < val * 10)
          end = len;
        else
          end = val * 10;
        this.listParkingAreaPage = this.listParkingArea.slice((val - 1) * 10, end);
      },
      handleDetail() {},
      openDialogEditParkingArea(index, row) {
        index = index + (this.currentPage - 1) * 10;
        this.parkingEditDialog = true;
        this.parkingAreaViewIndex = index
        this.parkingAreaEdit.id = row.id;
        this.parkingAreaEdit.name = row.name;
        this.listParkingArea.forEach(element => {
          if (element.id == row.id) {
            this.listParkingSlot = element.parkingSlots;
          }
        })
        // this.listParkingSlot = this.listParkingArea[index].parkingSlots;
        if (this.listParkingSlot.length > 10)
          this.listParkingSlotPage = this.listParkingSlot.slice(0, 10);
        else {
          this.listParkingSlotPage = this.listParkingSlot.slice(0, this.listParkingSlot.length);
        }
      },
      saveParkingArea() {

      },
      showParkingDetail(index, row) {
        index = index + (this.currentPage - 1) * 10;
        this.viewNumber = [];
        this.parkingAreaDetail = true;
        this.parkingAreaViewIndex = index;
        index = index +
          this.getViewDataParkingArea(index);
      },
      getViewDataParkingArea(index) {
        var len = this.listParkingArea[index].parkingSlots.length;
        var idx = 0;
        var indexView = 0;
        var indexCol = 0;
        while (idx != len) {
          this.viewNumber[indexView] = []
          for (let j = 0; j < 8; j++) {
            var newCol = [];
            this.viewNumber[indexView][indexCol] = [];
            for (let i = 0; i < 8; i++) {
              newCol.push(this.listParkingArea[index].parkingSlots[idx])
              idx++;
              if (idx >= len)
                break;
            }
            this.viewNumber[indexView][indexCol] = newCol;
            if (idx >= len)
              break;
            indexCol++;
          }
          indexView++;
        }
      },
      reloadParkingView() {
        var id = this.listParkingArea[this.parkingAreaViewIndex].id;
        this.getParkingSlotsByIdService(id).then(data3 => {
          this.listParkingArea[this.parkingAreaViewIndex].parkingSlots = data3;
          this.listParkingArea[this.parkingAreaViewIndex].maxNumber = data3.length;
          this.getViewDataParkingArea(this.parkingAreaViewIndex);
          this.parkingAreaDetail = false;
          this.parkingAreaDetail = true;
        })

      },
      handleParkingAreaData(data) {
        this.listParkingArea = []
        this.listParkingAreaPage = []
        const arealen = data.length
        if (arealen == 0)
          return;
        data.forEach((element) => {
          element.createdDate = this.dateFormat(element.createdDate);
          this.getEmptySlotByParkingAreaId(element.id).then(data2 => {
            element.numNotInUsed = data2;
            this.getParkingSlotsByIdService(element.id).then(data3 => {
              element.parkingSlots = data3
              element.maxNumber = data3.length;
              this.listParkingArea.push(element)
              if (this.listParkingArea.length == arealen) {
                this.listParkingArea = this.listParkingArea.sort((a, b) => (a.id > b.id) ? 1 : -1)
                this.handleCurrentChangeArea(1)
              }
            })

          })
        })
      },
      reloadParkingArea() {
        this.loadingArea = false
        this.listParkingArea = [];
        this.getParkingAreaService().then(data => {
          this.handleParkingAreaData(data);
        })
      },
      getListParkingSlot() {
        this.getAllParkingSlot().then(data => {
          data
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
    },
    computed: {
      tableFilter() {
        if (this.tableData) {
          this.tableData.forEach((element) => {
            if (element.type === 1) {
              element.type = "xe máy";
            } else {
              element.type = "ô tô";
            }
          });
        }
      },
    },
    created() {
      this.reloadParkingArea();
    }
  };

</script>
