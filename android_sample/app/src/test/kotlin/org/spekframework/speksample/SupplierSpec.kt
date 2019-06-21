package org.spekframework.speksample

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object SupplierSpec : Spek({
    describe("A valid supplier") {
        val supplier by memoized {
            SupplierValidator(supplier = Supplier(
                    coordinates = floatArrayOf(1f, 2f),
                    priceP2 = 1.0,//: Double,
                    priceP13 = 2.0,
                    priceP45 = 3.0,
                    _id = "whatever",
                    address = "Asdrdrses",
                    name = "Namezoid",
                    scoresAvg = 0.0,
                    phone = "(66)6666.6666",
                    distance = 0.0,
                    isFavorite = false
            ))
        }
        it("should not have \"invalid\" coordinates") {
            supplier.supplier.coordinates = floatArrayOf(0f, 0f)
            assertEquals(expected = false, actual = supplier.validateCoordinates())
        }
        it("should have \"valid\" coordinates") {
            assertEquals(expected = true, actual = supplier.validateCoordinates())
        }

        it("should have an address") {
            assertEquals(expected = false, actual = supplier.addressIsBlank())

            supplier.supplier.address = ""
            assertEquals(expected = true, actual = supplier.addressIsBlank())

        }

        it("should have a phone") {
            assertEquals(expected = false, actual = supplier.phoneIsBlank())

            supplier.supplier.phone = ""
            assertEquals(expected = true, actual = supplier.phoneIsBlank())
        }

        it("should have a name") {
            assertEquals(expected = false, actual = supplier.nameIsBlank())

            supplier.supplier.name = ""
            assertEquals(expected = true, actual = supplier.nameIsBlank())
        }

        it("should have at least one gas price") {

            supplier.supplier.priceP2 = 0.0
            supplier.supplier.priceP13 = 0.0
            supplier.supplier.priceP45 = 0.0

            assertEquals(expected = false, actual = supplier.validatePrices())

            supplier.supplier.priceP13 = 1.0
            assertEquals(expected = true, actual = supplier.validatePrices())
        }

    }
    Feature("Validations when adding a supplier") {
        val supplier by memoized {
            SupplierValidator(supplier = Supplier(
                    coordinates = floatArrayOf(1f, 2f),
                    priceP2 = 1.0,//: Double,
                    priceP13 = 2.0,
                    priceP45 = 3.0,
                    _id = "whatever",
                    address = "Asdrdrses",
                    name = "Namezoid",
                    scoresAvg = 0.0,
                    phone = "(66)6666.6666",
                    distance = 0.0,
                    isFavorite = false
            ))
        }

        Scenario("invalid coordinates should not validate") {
            var result = false
            Given("a supplier with invalid coordinates") {
                supplier.supplier.coordinates = floatArrayOf(0f, 0f)
            }

            When("the supplier is validated") {
                result = supplier.validateSupplier()
            }

            Then("it should not be allowed to be added") {
                assertEquals(false, result)
            }

        }

        Scenario("empty names should not validate") {
            var result = false
            Given("a supplier with an empty name") {
                supplier.supplier.name = ""
            }

            When("the supplier is validated") {
                result = supplier.validateSupplier()
            }

            Then("it should not be allowed to be added") {
                assertEquals(false, result)
            }

        }

        Scenario("empty addresses should not validate") {
            var result = false
            Given("a supplier with an empty address") {
                supplier.supplier.address = ""
            }

            When("the supplier is validated") {
                result = supplier.validateSupplier()
            }

            Then("it should not be allowed to be added") {
                assertEquals(false, result)
            }
        }

        Scenario("empty phone numbers should not validate") {
            var result = false
            Given("a supplier with an empty phone number") {
                supplier.supplier.phone = ""
            }

            When("the supplier is validated") {
                result = supplier.validateSupplier()
            }

            Then("it should not be allowed to be added") {
                assertEquals(false, result)
            }
        }

        Scenario("empty gas prices should not validate") {
            var result = false
            Given("a supplier with no gas prices") {
                supplier.supplier.priceP2 = 0.0
                supplier.supplier.priceP13 = 0.0
                supplier.supplier.priceP45 = 0.0
            }

            When("the supplier is validated") {
                result = supplier.validateSupplier()
            }

            Then("it should not be allowed to be added") {
                assertEquals(false, result)
            }
        }
    }
})
