package org.spekframework.speksample

class SupplierValidator(val supplier: Supplier){

    public fun validateCoordinates(): Boolean {
        if (supplier.coordinates[0] == 0f && supplier.coordinates[1] == 0f) {
            //Snackbar.make(layoutAddSupplier, "Endereço não encontrado.", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true //retorna false se os 2 forem 0 (n achou endereço ou algo assim), que é no meio do oceano e parece ser um local complicado de algum dia existir fornecedor lá
    }

    public fun validateData(): Boolean {
        if (nameIsBlank() || addressIsBlank() || phoneIsBlank()) {
            //Snackbar.make(layoutAddSupplier, "Todos os dados são obrigatórios.", Snackbar.LENGTH_LONG).show()
            return false
        }
        if (!validatePrices()) {
            return false
        }
        return true
    }

    public fun nameIsBlank(): Boolean = supplier.name.isBlank()

    public fun addressIsBlank(): Boolean = supplier.address.isBlank()

    public fun phoneIsBlank(): Boolean = supplier.phone.isBlank()


    public fun validatePrices(): Boolean{
        if (supplier.priceP13 == 0.0 && supplier.priceP2 == 0.0 && supplier.priceP45 == 0.0) {
            //Snackbar.make(layoutAddSupplier, "O Distribuidor deve possuir ao menos um tipo de gás.", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }
}