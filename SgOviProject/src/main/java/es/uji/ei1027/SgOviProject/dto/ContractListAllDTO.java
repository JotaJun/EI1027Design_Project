package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.model.Contract;

public class ContractListAllDTO {

    private Contract contract;
    private String contractedName;
    private String oviUserName;
    private String oviUserDni;

    public ContractListAllDTO(Contract contract, String name){
        this.contract = contract;
        this.contractedName = name;
    }

    public ContractListAllDTO(Contract contract, String contractedName, String oviUserName, String oviUserDni) {
        this.contract = contract;
        this.contractedName = contractedName;
        this.oviUserName = oviUserName;
        this.oviUserDni = oviUserDni;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getContractedName() {
        return contractedName;
    }

    public void setContractedName(String contractedName) {
        this.contractedName = contractedName;
    }

    public String getOviUserName() {
        return oviUserName;
    }

    public void setOviUserName(String oviUserName) {
        this.oviUserName = oviUserName;
    }

    public String getOviUserDni() {
        return oviUserDni;
    }

    public void setOviUserDni(String oviUserDni) {
        this.oviUserDni = oviUserDni;
    }

    @Override
    public String toString() {
        return "ContractListAllDTO{" +
                "contract=" + contract +
                ", contractedName='" + contractedName + '\'' +
                ", oviUserName='" + oviUserName + '\'' +
                ", oviUserDni='" + oviUserDni + '\'' +
                '}';
    }
}
