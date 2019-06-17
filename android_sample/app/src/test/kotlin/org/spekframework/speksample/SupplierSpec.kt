package org.spekframework.speksample

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SupplierSpec : Spek({
    describe("A valid supplier") {
        val supplier by memoized { SupplierValidator( supplier = Supplier(
                coordinates = floatArrayOf(1f , 2f),
                priceP2 = 1.0,//: Double,
                priceP13 = 2.0,
                priceP45 = 3.0,
                _id = "whatever",
                address = "Asdrdrses",
                name = "Namezoid",
                scoresAvg = 0.0,
                phone = "0129375656278237590267934559562",
                distance = 0.0,
                isFavorite = false
        ))}

        it("should not have \"invalid\" coordinates") {
            supplier.supplier.coordinates = floatArrayOf(0f, 0f)
            assertEquals(expected = false, actual = supplier.validateCoordinates())
        }
        it("should have \"valid\" coordinates") {
            assertEquals(expected = true, actual = supplier.validateCoordinates())
        }

        it("should have an address"){
            assertEquals(expected = false, actual = supplier.addressIsBlank())

            supplier.supplier.address = ""
            assertEquals(expected = true, actual = supplier.addressIsBlank())

        }

        it("should have a phone"){
            assertEquals(expected = false, actual = supplier.phoneIsBlank())

            supplier.supplier.phone = ""
            assertEquals(expected = true, actual = supplier.phoneIsBlank())
        }

        it("should have a name"){
            assertEquals(expected = false, actual = supplier.nameIsBlank())

            supplier.supplier.name = ""
            assertEquals(expected = true, actual = supplier.nameIsBlank())
        }

        it("should be                        valid)") {
            assertEquals(expected = true, actual = supplier.validateData() && supplier.validateCoordinates())
        }
    }
})
