package twm.crdb.v1.remote;

import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import com.vv.personal.twm.crdb.v1.data.dao.VillaDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vivek
 * @since 15/01/22
 */
@Slf4j
@RequiredArgsConstructor
@RestController("villa-controller")
@RequestMapping("/crdb/bank/fd")
public class BankFixedDepositController {

    private final VillaDao villaDao;

    @PostMapping("/add-villas")
    public String addVillas(@RequestBody VillaProto.VillaList villaList) {
        log.info("Received request to add '{}' villas into db", villaList.getVillasCount());
        try {
            villaDao.addVillas(villaList);
            log.info("Done!");
            return "Done";
        } catch (Exception e) {
            log.error("Failed to write all data correctly for {} villas from villaList. ", villaList.getVillasCount(), e);
        }
        return "Failed";
    }

    @GetMapping("/read/all")
    public VillaProto.VillaList readAllVillasFromCrdb(@RequestParam String world) {
        log.info("Received request to read all villas from world '{}' from db", world);
        try {
            return villaDao.getVillas(world);
        } catch (Exception e) {
            log.error("Failed to get all villas for {}. ", world, e);
        }
        return VillaProto.VillaList.newBuilder().build();
    }

    /*@GetMapping("/test")
    public void test() {
        VillaProto.Villa villa = VillaProto.Villa.newBuilder()
                .setName("dataline")
                .setWorld("enp999")
                .setFarmStrength("24000/24000")
                .setX(999)
                .setY(999)
                .setId("99999")
                .setTroops(VillaProto.Troops.newBuilder()
                        .setNb(99)
                        .setWl(20)
                        .setSp(999)
                        .setSw(999)
                        .setAx(999)
                        .setAr(999)
                        .setSu(999)
                        .setLc(999)
                        .setMa(999)
                        .setHc(999)
                        .setRm(999)
                        .setCt(999)
                        .setPd(999)
                        .build())
                .build();

        villaDao.addVillas(VillaProto.VillaList.newBuilder()
                .addVillas(villa)
                .build());
    }

    @GetMapping("/test-2")
    public String test2() {
        return villaDao.getVillas("enp999").getVillasList().toString();
    }*/
}