package es.uji.ei1027.SgOviProject.dto;

import es.uji.ei1027.SgOviProject.enums.StaffType;
import es.uji.ei1027.SgOviProject.model.Contract;

public class ContractListAllDTO {

    private Contract contract;
    private String contractedName;

    public ContractListAllDTO(Contract contract, String name){
        this.contract = contract;
        this.contractedName = name;
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

    @Override
    public String toString() {
        return "ContractListAllDTO{" +
                "contract=" + contract +
                ", contractedName='" + contractedName + '\'' +
                '}';
    }
}
