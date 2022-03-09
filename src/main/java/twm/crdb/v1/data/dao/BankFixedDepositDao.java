package twm.crdb.v1.data.dao;

import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import com.vv.personal.twm.crdb.v1.data.entity.VillaEntity;
import com.vv.personal.twm.crdb.v1.data.entity.VillaIdentifier;
import com.vv.personal.twm.crdb.v1.data.repository.VillaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import twm.crdb.v1.data.repository.BankFixedDepositRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vivek
 * @since 15/01/22
 */
@Component
@RequiredArgsConstructor
public class BankFixedDepositDao {
    private final BankFixedDepositRepository bankFixedDepositRepository;

    public void addVillas(VillaProto.VillaList villaList) {
        Instant currentTime = Instant.now();
        List<VillaEntity> villaEntities = villaList.getVillasList().stream()
                .map(villa -> generateVillaEntity(villa, currentTime)).collect(Collectors.toList());
        bankFixedDepositRepository.saveAll(villaEntities);
    }

    public VillaProto.VillaList getVillas(String world) {
        List<VillaEntity> villaEntities = bankFixedDepositRepository.findAllByVillaIdentifierWorld(world);

        return VillaProto.VillaList.newBuilder().addAllVillas(
                villaEntities.stream().map(
                        villaEntity -> VillaProto.Villa.newBuilder()
                                .setId(villaEntity.getVillaId())
                                .setX(villaEntity.getVillaIdentifier().getX())
                                .setY(villaEntity.getVillaIdentifier().getY())
                                .setType(VillaProto.VillaType.valueOf(villaEntity.getVillaType()))
                                .setWorld(villaEntity.getVillaIdentifier().getWorld())
                                .setName(villaEntity.getVillaName())
                                .setFarmStrength(villaEntity.getFarmStrength())
                                .setTimestamp(villaEntity.getVillaIdentifier().getCreatedTimestamp().toEpochMilli())
                                .setTroops(VillaProto.Troops.newBuilder()
                                        .setWl(villaEntity.getWall())
                                        .setSp(villaEntity.getSpear())
                                        .setSw(villaEntity.getSword())
                                        .setAx(villaEntity.getAxe())
                                        .setAr(villaEntity.getArch())
                                        .setSu(villaEntity.getScout())
                                        .setLc(villaEntity.getLcav())
                                        .setMa(villaEntity.getMarc())
                                        .setHc(villaEntity.getHcav())
                                        .setRm(villaEntity.getRam())
                                        .setCt(villaEntity.getCat())
                                        .setPd(villaEntity.getPalad())
                                        .setNb(villaEntity.getNoble())
                                        .build())
                                .build()
                ).collect(Collectors.toList())
        ).build();
    }

    private VillaEntity generateVillaEntity(VillaProto.Villa villa, Instant instant) {
        VillaIdentifier villaIdentifier = new VillaIdentifier()
                .setCreatedTimestamp(instant)
                .setWorld(villa.getWorld())
                .setX(villa.getX())
                .setY(villa.getY());

        return new VillaEntity()
                .setVillaIdentifier(villaIdentifier)
                .setVillaName(villa.getName())
                .setVillaId(villa.getId())
                .setVillaType(villa.getType().name())
                .setFarmStrength(villa.getFarmStrength())

                .setWall(villa.getTroops().getWl())
                .setSpear(villa.getTroops().getSp())
                .setSword(villa.getTroops().getSw())
                .setAxe(villa.getTroops().getAx())
                .setArch(villa.getTroops().getAr())
                .setScout(villa.getTroops().getSu())
                .setLcav(villa.getTroops().getLc())
                .setMarc(villa.getTroops().getMa())
                .setHcav(villa.getTroops().getHc())
                .setRam(villa.getTroops().getRm())
                .setCat(villa.getTroops().getCt())
                .setPalad(villa.getTroops().getPd())
                .setNoble(villa.getTroops().getNb());
    }
}