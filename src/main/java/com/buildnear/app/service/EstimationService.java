package com.buildnear.app.service;

import com.buildnear.app.dto.EstimationDto;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class EstimationService {

    public EstimationDto estimateHouseConstruction(EstimationDto input) {
        double area = input.getBuiltUpAreaSqFt() != null && input.getBuiltUpAreaSqFt() > 0 ? input.getBuiltUpAreaSqFt() : 1200.0;
        int floors = input.getFloorsCount() != null && input.getFloorsCount() > 0 ? input.getFloorsCount() : 1;
        String tier = input.getPackageTier() != null ? input.getPackageTier().toUpperCase() : "STANDARD";

        double ratePerSqFt;
        String timeline;
        String materialGrade;

        switch (tier) {
            case "BASIC":
                ratePerSqFt = 1650.0;
                timeline = (6 + floors * 2) + " Months";
                materialGrade = "Standard 53-grade Cement, Fe500 TMT Steel, Ceramic Tiles, Anchor Switches";
                break;
            case "LUXURY":
                ratePerSqFt = 2850.0;
                timeline = (9 + floors * 3) + " Months";
                materialGrade = "UltraTech Super, Fe550D Primary TMT, Italian Marble / GVT Tiles, Legrand Automation, Kohler Bathware";
                break;
            case "STANDARD":
            default:
                tier = "STANDARD";
                ratePerSqFt = 2150.0;
                timeline = (7 + floors * 2) + " Months";
                materialGrade = "UltraTech / ACC Cement, Tata Tiscon Fe500D, Vitrified Tiles, Havells Modular, Jaquar Fittings";
                break;
        }

        double totalArea = area * floors;
        double totalCost = Math.round(totalArea * ratePerSqFt);

        EstimationDto result = new EstimationDto();
        result.setEstimationType("HOUSE_CONSTRUCTION");
        result.setBuiltUpAreaSqFt(totalArea);
        result.setFloorsCount(floors);
        result.setPackageTier(tier);
        result.setRatePerUnit(ratePerSqFt);
        result.setUnitName("₹ / sq.ft");
        result.setTotalEstimatedCost(totalCost);
        result.setEstimatedTimeline(timeline);
        result.setDisclaimer("ESTIMATED PRICE ONLY: This is an indicative cost estimate based on standard regional specifications. Actual contractor quotations may vary depending on soil conditions, architectural designs, and customized material finishes.");

        Map<String, Double> breakdown = new LinkedHashMap<>();
        breakdown.put("Civil Structure & RCC Foundation (52%)", (double) Math.round(totalCost * 0.52));
        breakdown.put("Finishing, Plaster & Flooring (20%)", (double) Math.round(totalCost * 0.20));
        breakdown.put("Electrical Conduit & Linework (12%)", (double) Math.round(totalCost * 0.12));
        breakdown.put("Plumbing, Drainage & Sanitary (9%)", (double) Math.round(totalCost * 0.09));
        breakdown.put("Architectural Drawings & Permits (7%)", (double) Math.round(totalCost * 0.07));
        result.setCostBreakdown(breakdown);

        Map<String, String> details = new LinkedHashMap<>();
        details.put("Floors & Layout", floors + " Floor(s), " + totalArea + " total built-up sq.ft");
        details.put("Material Grade", materialGrade);
        details.put("Warranty", "10-year structural warranty, 1-year seepage warranty");
        details.put("Payment Stages", "Milestone-based (Foundation 20%, RCC Slab 25%, Brickwork 20%, Finishing 25%, Handover 10%)");
        result.setItemizedDetails(details);

        return result;
    }

    public EstimationDto estimateInteriorDesign(EstimationDto input) {
        String homeType = input.getHomeType() != null ? input.getHomeType().toUpperCase() : "2BHK";
        String scope = input.getInteriorScope() != null ? input.getInteriorScope().toUpperCase() : "FULL_HOME";

        double baseCost;
        String timeline;
        String scopeTitle;

        // Base costs for 2BHK
        switch (homeType) {
            case "1BHK":
                baseCost = 280000.0;
                break;
            case "3BHK":
                baseCost = 550000.0;
                break;
            case "4BHK_VILLA":
            case "VILLA":
                homeType = "4BHK_VILLA";
                baseCost = 850000.0;
                break;
            case "2BHK":
            default:
                homeType = "2BHK";
                baseCost = 420000.0;
                break;
        }

        double multiplier;
        switch (scope) {
            case "MODULAR_KITCHEN":
                multiplier = 0.45;
                timeline = "20 - 25 Days";
                scopeTitle = "Modular Kitchen Only (Acrylic / Marine Ply with Hettich hardware)";
                break;
            case "ESSENTIALS":
                multiplier = 0.70;
                timeline = "30 - 35 Days";
                scopeTitle = "Essentials (Kitchen + Master Wardrobe + Basic False Ceiling)";
                break;
            case "LUXURY":
                multiplier = 1.65;
                timeline = "50 - 65 Days";
                scopeTitle = "Luxury Turnkey Interior (Designer PU finishes, Smart Lighting, Full Veneer & Wallpaper)";
                break;
            case "FULL_HOME":
            default:
                scope = "FULL_HOME";
                multiplier = 1.0;
                timeline = "40 - 45 Days";
                scopeTitle = "Complete Home Interior (Kitchen, All Bedrooms Wardrobes, Living TV Unit, False Ceiling & Painting)";
                break;
        }

        double totalCost = Math.round(baseCost * multiplier);

        EstimationDto result = new EstimationDto();
        result.setEstimationType("INTERIOR_DESIGN");
        result.setHomeType(homeType);
        result.setInteriorScope(scope);
        result.setTotalEstimatedCost(totalCost);
        result.setEstimatedTimeline(timeline);
        result.setUnitName("Total Package Estimate");
        result.setDisclaimer("ESTIMATED PRICE ONLY: Actual quotations will be customized after detailed site measurements, 3D render approval, and choice of hardware/surface laminates.");

        Map<String, Double> breakdown = new LinkedHashMap<>();
        if ("MODULAR_KITCHEN".equals(scope)) {
            breakdown.put("Base Cabinets & Carcass (40%)", (double) Math.round(totalCost * 0.40));
            breakdown.put("Overhead & Loft Cabinets (25%)", (double) Math.round(totalCost * 0.25));
            breakdown.put("Soft-Close Baskets & Hardware (20%)", (double) Math.round(totalCost * 0.20));
            breakdown.put("Quartz / Granite Countertop & Sink (15%)", (double) Math.round(totalCost * 0.15));
        } else {
            breakdown.put("Modular Kitchen & Countertops (30%)", (double) Math.round(totalCost * 0.30));
            breakdown.put("Wardrobes & Storage Units (32%)", (double) Math.round(totalCost * 0.32));
            breakdown.put("Designer False Ceiling & Ambient LED (15%)", (double) Math.round(totalCost * 0.15));
            breakdown.put("Wall Paneling, TV Unit & Furniture (13%)", (double) Math.round(totalCost * 0.13));
            breakdown.put("Luxury Interior Painting & Finishes (10%)", (double) Math.round(totalCost * 0.10));
        }
        result.setCostBreakdown(breakdown);

        Map<String, String> details = new LinkedHashMap<>();
        details.put("Config", homeType + " - " + scopeTitle);
        details.put("Woodwork Core", "BWP Marine Grade Plywood (IS 710) with Anti-termite warranty");
        details.put("Hardware Brands", "Hettich / Hafele Soft-Close Hinges and Tandem Drawers");
        details.put("Warranty", "10-Year Flat Warranty on Modular Woodwork");
        result.setItemizedDetails(details);

        return result;
    }
}
