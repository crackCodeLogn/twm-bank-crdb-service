package twm.crdb.v1.data.dao;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import com.vv.personal.twm.crdb.v1.data.entity.VillaEntity;
import com.vv.personal.twm.crdb.v1.data.entity.VillaIdentifier;
import com.vv.personal.twm.crdb.v1.data.repository.VillaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import twm.crdb.v1.data.entity.BankEntity;
import twm.crdb.v1.data.repository.BankRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vivek
 * @since 15/01/22
 */
@Component
@RequiredArgsConstructor
public class BankDao {
    private final BankRepository bankRepository;

    public void addBank(BankProto.Bank bank) {
        Instant currentTime = Instant.now();
        BankEntity bankEntity = generateBankEntity(bank, currentTime);
        bankRepository.save(bankEntity);
    }

    public VillaProto.VillaList getVillas(String world) {
        List<VillaEntity> villaEntities = bankRepository.findAllByVillaIdentifierWorld(world);

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

    private BankEntity generateBankEntity(BankProto.Bank bank, Instant instant) {
        return new BankEntity()
                .setBankName(bank.getName())
                .setBankType(bank.getBankType())
                .setIfsc(bank.getIFSC())
                .setContactNumber(bank.getContactNumber())
                .setCreatedTimestamp(instant);
    }
}