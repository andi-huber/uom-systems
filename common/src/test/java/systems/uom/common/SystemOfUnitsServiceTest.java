/*
 * Units of Measurement Systems
 * Copyright (c) 2005-2017, Jean-Marie Dautelle, Werner Keil and others.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-363, Units of Measurement nor the names of their contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package systems.uom.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.logging.Logger;

import javax.measure.spi.ServiceProvider;
import javax.measure.spi.SystemOfUnits;
import javax.measure.spi.SystemOfUnitsService;

import org.junit.BeforeClass;
import org.junit.Test;

public class SystemOfUnitsServiceTest {
	private static final String DEFAULT_SERVICE_CLASSNAME = "tech.units.indriya.internal.DefaultSystemOfUnitsService";
	private static final String COMMON_SERVICE_CLASSNAME = "systems.uom.common.internal.CommonSystemService";
	private static final Logger LOGGER = Logger.getLogger(SystemOfUnitsServiceTest.class.getName());

	private static final int NUM_OF_UNITS_DEFAULT = 43;
	private static final int NUM_OF_UNITS_US = 45;
	private static final int NUM_OF_UNITS_CGS = 12;

	private static SystemOfUnitsService defaultService;

	@BeforeClass
	public static void setUp() {
		defaultService = ServiceProvider.current().getSystemOfUnitsService();
	}

	@Test
	public void testDefaultUnitSystemService() {
		assertNotNull(defaultService);
		assertEquals(DEFAULT_SERVICE_CLASSNAME, defaultService.getClass().getName());
		SystemOfUnits system = defaultService.getSystemOfUnits();
		assertNotNull(system);
		assertEquals("tech.units.indriya.unit.Units", system.getClass().getName());
		assertEquals("Units", system.getName());
		assertNotNull(system.getUnits());
		assertEquals(NUM_OF_UNITS_DEFAULT, system.getUnits().size());
	}
	
	@Test
	public void testCommonUnitSystemService() {
		final ServiceProvider commonProvider = ServiceProvider.of("Common");
		assertNotNull(commonProvider);
		final SystemOfUnitsService commonService = commonProvider.getSystemOfUnitsService();
		assertNotNull(commonService);
		assertEquals("systems.uom.common.internal.CommonSystemService", commonService.getClass().getName());
		SystemOfUnits system = commonService.getSystemOfUnits();
		assertNotNull(system);
		assertEquals("systems.uom.common.USCustomary", system.getClass().getName());
		assertEquals("United States Customary Units", system.getName());
		assertNotNull(system.getUnits());
		assertEquals(NUM_OF_UNITS_US, system.getUnits().size());
	}

	@Test
	// TODO consolidate asserts
	public void testCommonUnitSystemServiceAlias() {
		final ServiceProvider commonProvider = ServiceProvider.of("Common");
		assertNotNull(commonProvider);
		final SystemOfUnitsService commonService = commonProvider.getSystemOfUnitsService();
		assertNotNull(commonService);
		assertEquals(COMMON_SERVICE_CLASSNAME, commonService.getClass().getName());
		SystemOfUnits system = commonService.getSystemOfUnits("USCustomary");
		assertNotNull(system);
		assertEquals("systems.uom.common.USCustomary", system.getClass().getName());
		assertEquals("United States Customary Units", system.getName());
		assertNotNull(system.getUnits());
		assertEquals(NUM_OF_UNITS_US, system.getUnits().size());
		SystemOfUnits system2 = commonService.getSystemOfUnits("US");
		assertEquals(system, system2);

		system = commonService.getSystemOfUnits("CGS");
		assertNotNull(system);
		assertEquals("Centimetre–gram–second System of Units", system.getName());
		system2 = commonService.getSystemOfUnits("Centimetre–gram–second");
		assertEquals(system, system2);
		assertEquals(NUM_OF_UNITS_CGS, system.getUnits().size());
	}

	@Test
	public void testOtherUnitSystemServices() {
		Collection<ServiceProvider> services = ServiceProvider.available();
		assertNotNull(services);
		assertEquals(3, services.size());
		for (ServiceProvider provider : services) {
			LOGGER.info(String.valueOf(provider));
			// TODO change to DEBUG or lower after https://github.com/unitsofmeasurement/unit-api/issues/195 was resolved
		}
		// for (SystemOfUnitsService service : services) {
		// checkService(service);
		// }
	}
	/*
	 * private void checkService(SystemOfUnitsService service) { SystemOfUnits
	 * system; switch (service.getClass().getName()) { case
	 * "systems.uom.iso80k.internal.ISO80kSystemService":
	 * assertEquals("systems.uom.iso80k.internal.ISO80kSystemService",
	 * service.getClass().getName());
	 * assertNotNull(service.getAvailableSystemsOfUnits()); assertEquals(1,
	 * service.getAvailableSystemsOfUnits().size()); system =
	 * service.getSystemOfUnits(); assertNotNull(system); assertEquals("ISO80000",
	 * system.getName()); system = service.getSystemOfUnits("ISO80000");
	 * assertNotNull(system); assertEquals("ISO80000", system.getName()); break;
	 * case "systems.uom.common.internal.CommonSystemService":
	 * assertEquals("systems.uom.common.internal.CommonSystemService",
	 * service.getClass().getName());
	 * assertNotNull(service.getAvailableSystemsOfUnits()); assertEquals(2,
	 * service.getAvailableSystemsOfUnits().size()); system =
	 * service.getSystemOfUnits(); assertNotNull(system); assertEquals("US",
	 * system.getName()); system = service.getSystemOfUnits("Imperial");
	 * assertNotNull(system); assertEquals("Imperial", system.getName()); break;
	 * case "si.uom.impl.SISystemService":
	 * assertEquals("si.uom.impl.SISystemService", service.getClass() .getName());
	 * assertNotNull(service.getAvailableSystemsOfUnits()); assertEquals(1,
	 * service.getAvailableSystemsOfUnits().size()); system =
	 * service.getSystemOfUnits(); assertNotNull(system); assertEquals("SI",
	 * system.getName()); break; case
	 * "tech.units.indriya.internal.DefaultSystemOfUnitsService":
	 * assertEquals("tech.units.indriya.internal.DefaultSystemOfUnitsService",
	 * service.getClass().getName());
	 * assertNotNull(service.getAvailableSystemsOfUnits()); assertEquals(1,
	 * service.getAvailableSystemsOfUnits().size()); system =
	 * service.getSystemOfUnits(); assertNotNull(system); assertEquals("Units",
	 * system.getName()); break; default: break; } }
	 */
}
