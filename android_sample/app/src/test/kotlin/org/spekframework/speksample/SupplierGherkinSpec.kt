package org.spekframework.speksample

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SupplierGherkinSpec : Spek({
    describe("A supplier") {
        val supplier by memoized { SupplierValidator( supplier = Supplier(
                coordinates = floatArrayOf(0f , 0f),
                priceP2 = 0.0,//: Double,
                priceP13 = 0.0,
                priceP45 = 0.0,
                _id = "",
                address = "",
                name = "",
                scoresAvg = 0.0,
                phone = "",
                distance = 0.0,
                isFavorite = false
        ))}

        it("should be invalid") {

            assertEquals(expected = false, actual = supplier.validateCoordinates())
        }
        it("should be valid") {
            assertEquals(expected = true, actual = supplier.validateData())
        }
    }
})
