package com.buildnear.app.config;

import com.buildnear.app.entity.*;
import com.buildnear.app.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final ProfessionalProfileRepository professionalProfileRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final QuotationRepository quotationRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      ProfessionalProfileRepository professionalProfileRepository,
                      PortfolioItemRepository portfolioItemRepository,
                      ServicePackageRepository servicePackageRepository,
                      ServiceRequestRepository serviceRequestRepository,
                      QuotationRepository quotationRepository,
                      BookingRepository bookingRepository,
                      ReviewRepository reviewRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.professionalProfileRepository = professionalProfileRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.servicePackageRepository = servicePackageRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.quotationRepository = quotationRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            logger.info("Database has existing records. Checking and populating nationwide specialists...");
            seedNationwideProfessionals();
            logger.info("Nationwide seeding verified.");
            return;
        }

        logger.info("Initializing BuildNear sample data (Development Dataset)...");

        // 1. Admin
        User admin = new User(
                "admin@buildnear.com",
                passwordEncoder.encode("Admin@123"),
                "BuildNear Admin",
                "+91 98200 11000",
                Role.ROLE_ADMIN,
                "Mumbai",
                "BuildNear HQ, Bandra Kurla Complex",
                19.0660,
                72.8680
        );
        userRepository.save(admin);

        // 2. Customers
        User customer1 = new User(
                "customer@buildnear.com",
                passwordEncoder.encode("Customer@123"),
                "Aditi Sharma",
                "+91 98765 43210",
                Role.ROLE_CUSTOMER,
                "Mumbai",
                "A-402, Sea Green Apartments, Andheri West",
                19.1136,
                72.8697
        );
        User customer2 = new User(
                "rohit.customer@buildnear.com",
                passwordEncoder.encode("Customer@123"),
                "Rohit Verma",
                "+91 98111 22334",
                Role.ROLE_CUSTOMER,
                "Bengaluru",
                "124, 4th Cross, Indiranagar",
                12.9716,
                77.5946
        );
        User customer3 = new User(
                "priya.customer@buildnear.com",
                passwordEncoder.encode("Customer@123"),
                "Priya Nair",
                "+91 97654 32109",
                Role.ROLE_CUSTOMER,
                "Pune",
                "Flat 12, Baner Hills Road",
                18.5204,
                73.8567
        );
        userRepository.saveAll(Arrays.asList(customer1, customer2, customer3));

        // 3. Professionals
        // Pro 1: Architect
        User uPro1 = new User("arun.architect@buildnear.com", passwordEncoder.encode("Pro@123"), "Ar. Arun Kulkarni", "+91 98201 55443", Role.ROLE_PROFESSIONAL, "Mumbai", "Studio Kulkarni, Dadar West", 19.0178, 72.8478);
        userRepository.save(uPro1);
        ProfessionalProfile p1 = new ProfessionalProfile();
        p1.setUser(uPro1);
        p1.setCategory("ARCHITECT");
        p1.setCategoryTitle("Senior Residential Architect & 3D Planner");
        p1.setBio("Licensed architect with 12+ years of experience designing contemporary sustainable villas, duplex layouts, and municipal sanctioned blueprints.");
        p1.setExperienceYears(12);
        p1.setQualifications("B.Arch (Sir J.J. College), Registered Council of Architecture (CA/2012/54821)");
        p1.setSkills("Vastu Shastra, 3D Elevation, Structural Drawing, Municipal Sanctions, Floor Planning");
        p1.setMinimumPrice(15.0);
        p1.setPricingUnit(PricingUnit.PER_SQFT);
        p1.setServiceRadiusKm(30.0);
        p1.setAvailability(AvailabilityStatus.AVAILABLE);
        p1.setVerifiedByAdmin(true);
        p1.setAverageRating(4.9);
        p1.setReviewCount(28);
        p1.setAvatarUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150");
        p1.setYearsInArea(8);
        professionalProfileRepository.save(p1);

        // Pro 2: Civil Engineer
        User uPro2 = new User("suresh.engineer@buildnear.com", passwordEncoder.encode("Pro@123"), "Er. Suresh Patel", "+91 98334 11223", Role.ROLE_PROFESSIONAL, "Mumbai", "Patel Infra Consultants, Goregaon East", 19.1663, 72.8526);
        userRepository.save(uPro2);
        ProfessionalProfile p2 = new ProfessionalProfile();
        p2.setUser(uPro2);
        p2.setCategory("CIVIL_ENGINEER");
        p2.setCategoryTitle("Structural Consultant & Turnkey Contractor");
        p2.setBio("Certified civil engineer specializing in soil testing, earthquake-resistant RCC frame construction, and site supervision.");
        p2.setExperienceYears(15);
        p2.setQualifications("M.Tech Structural Engineering, Licensed Structural Engineer Grade-1");
        p2.setSkills("Soil Testing, Column Layout, RCC Slab Design, Foundation Engineering, Site Audit");
        p2.setMinimumPrice(1850.0);
        p2.setPricingUnit(PricingUnit.PER_SQFT);
        p2.setServiceRadiusKm(40.0);
        p2.setAvailability(AvailabilityStatus.AVAILABLE);
        p2.setVerifiedByAdmin(true);
        p2.setAverageRating(4.8);
        p2.setReviewCount(34);
        p2.setAvatarUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150");
        p2.setYearsInArea(12);
        professionalProfileRepository.save(p2);

        // Pro 3: Master Mason
        User uPro3 = new User("rajesh.mason@buildnear.com", passwordEncoder.encode("Pro@123"), "Rajesh Kumar", "+91 98920 33445", Role.ROLE_PROFESSIONAL, "Mumbai", "Shree Ram Nagar, Kurla West", 19.0728, 72.8797);
        userRepository.save(uPro3);
        ProfessionalProfile p3 = new ProfessionalProfile();
        p3.setUser(uPro3);
        p3.setCategory("MASON");
        p3.setCategoryTitle("Master Mason & Brickwork Specialist");
        p3.setBio("Master mason with 16 years of expertise leading teams for red brick laying, AAC lightweight blocks, external waterproof plastering, and granite flooring.");
        p3.setExperienceYears(16);
        p3.setQualifications("Certified Lead Mason (CSDCI Certified)");
        p3.setSkills("Red Brick Masonry, AAC Block Laying, Plastering, Granite & Marble Fitting, Compound Walls");
        p3.setMinimumPrice(850.0);
        p3.setPricingUnit(PricingUnit.PER_DAY);
        p3.setServiceRadiusKm(25.0);
        p3.setAvailability(AvailabilityStatus.AVAILABLE);
        p3.setVerifiedByAdmin(true);
        p3.setAverageRating(4.7);
        p3.setReviewCount(42);
        p3.setAvatarUrl("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150");
        p3.setYearsInArea(10);
        professionalProfileRepository.save(p3);

        // Pro 4: Interior Designer
        User uPro4 = new User("ananya.interior@buildnear.com", passwordEncoder.encode("Pro@123"), "Ananya Roy", "+91 98450 77889", Role.ROLE_PROFESSIONAL, "Mumbai", "Studio Form Designs, Bandra West", 19.0596, 72.8295);
        userRepository.save(uPro4);
        ProfessionalProfile p4 = new ProfessionalProfile();
        p4.setUser(uPro4);
        p4.setCategory("INTERIOR_DESIGNER");
        p4.setCategoryTitle("Contemporary Interior Designer & Modular Stylist");
        p4.setBio("Award-winning interior stylist transforming 2BHK/3BHK apartments and villas into warm modern spaces with customized modular woodwork and atmospheric lighting.");
        p4.setExperienceYears(9);
        p4.setQualifications("Diploma in Interior Architecture & Styling (AID)");
        p4.setSkills("Modular Kitchens, 3D Walkthroughs, Wardrobe Carpentry, False Ceiling, Scandinavian Interiors");
        p4.setMinimumPrice(35000.0);
        p4.setPricingUnit(PricingUnit.PER_PROJECT);
        p4.setServiceRadiusKm(30.0);
        p4.setAvailability(AvailabilityStatus.AVAILABLE);
        p4.setVerifiedByAdmin(true);
        p4.setAverageRating(4.9);
        p4.setReviewCount(19);
        p4.setAvatarUrl("https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150");
        p4.setYearsInArea(6);
        professionalProfileRepository.save(p4);

        // Pro 5: Electrician
        User uPro5 = new User("vikram.electric@buildnear.com", passwordEncoder.encode("Pro@123"), "Vikram Solanki", "+91 97110 88990", Role.ROLE_PROFESSIONAL, "Mumbai", "Solanki Electricals, Malad West", 19.1860, 72.8485);
        userRepository.save(uPro5);
        ProfessionalProfile p5 = new ProfessionalProfile();
        p5.setUser(uPro5);
        p5.setCategory("ELECTRICIAN");
        p5.setCategoryTitle("Licensed Industrial & Home Electrician");
        p5.setBio("Licensed Wireman handling concealed wall wiring, MCB distribution board installation, inverter cabling, LED profile fixtures, and phase load balancing.");
        p5.setExperienceYears(11);
        p5.setQualifications("ITI Wireman License Grade-A (Govt. of Maharashtra)");
        p5.setSkills("Conduit Piping, 3-Phase Panel Wiring, Inverter Setup, Earthing, Line Fault Finding");
        p5.setMinimumPrice(450.0);
        p5.setPricingUnit(PricingUnit.PER_DAY);
        p5.setServiceRadiusKm(20.0);
        p5.setAvailability(AvailabilityStatus.AVAILABLE);
        p5.setVerifiedByAdmin(true);
        p5.setAverageRating(4.8);
        p5.setReviewCount(51);
        p5.setAvatarUrl("https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150");
        p5.setYearsInArea(9);
        professionalProfileRepository.save(p5);

        // Pro 6: Plumber
        User uPro6 = new User("manoj.plumber@buildnear.com", passwordEncoder.encode("Pro@123"), "Manoj Shinde", "+91 98212 99001", Role.ROLE_PROFESSIONAL, "Mumbai", "Shinde Sanitary Works, Chembur", 19.0522, 72.8994);
        userRepository.save(uPro6);
        ProfessionalProfile p6 = new ProfessionalProfile();
        p6.setUser(uPro6);
        p6.setCategory("PLUMBER");
        p6.setCategoryTitle("Sanitary & Concealed Plumbing Specialist");
        p6.setBio("Expert in multi-storey plumbing lines, solar water heater connections, booster pump installation, bathroom remodeling, and underground drainage slope laying.");
        p6.setExperienceYears(14);
        p6.setQualifications("Govt ITI Plumber Trade Certification");
        p6.setSkills("CPVC & UPVC Piping, Overhead Tank Installation, Bathroom Leakage Repair, Pressure Pumps");
        p6.setMinimumPrice(400.0);
        p6.setPricingUnit(PricingUnit.PER_DAY);
        p6.setServiceRadiusKm(25.0);
        p6.setAvailability(AvailabilityStatus.AVAILABLE);
        p6.setVerifiedByAdmin(true);
        p6.setAverageRating(4.6);
        p6.setReviewCount(38);
        p6.setAvatarUrl("https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150");
        p6.setYearsInArea(11);
        professionalProfileRepository.save(p6);

        // Pro 7: Carpenter
        User uPro7 = new User("harish.carpenter@buildnear.com", passwordEncoder.encode("Pro@123"), "Harish Mistry", "+91 98199 44332", Role.ROLE_PROFESSIONAL, "Mumbai", "Mistry Wood Works, Santacruz East", 19.0843, 72.8402);
        userRepository.save(uPro7);
        ProfessionalProfile p7 = new ProfessionalProfile();
        p7.setUser(uPro7);
        p7.setCategory("CARPENTER");
        p7.setCategoryTitle("Custom Furniture & Modular Woodworker");
        p7.setBio("Specialized in precision acrylic & laminate pressing on marine plywood, hydraulic bed mechanisms, sliding wardrobes, and acoustic wooden wall paneling.");
        p7.setExperienceYears(13);
        p7.setQualifications("Master Wood Artisan Trade Certificate");
        p7.setSkills("Plywood Wardrobes, Hydraulic Beds, Flush Door Fitting, Veneer & Laminate Pressing");
        p7.setMinimumPrice(800.0);
        p7.setPricingUnit(PricingUnit.PER_DAY);
        p7.setServiceRadiusKm(20.0);
        p7.setAvailability(AvailabilityStatus.AVAILABLE);
        p7.setVerifiedByAdmin(true);
        p7.setAverageRating(4.7);
        p7.setReviewCount(26);
        p7.setAvatarUrl("https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150");
        p7.setYearsInArea(8);
        professionalProfileRepository.save(p7);

        // Pro 8: Painter
        User uPro8 = new User("sunil.painter@buildnear.com", passwordEncoder.encode("Pro@123"), "Sunil Gupta", "+91 98700 66554", Role.ROLE_PROFESSIONAL, "Mumbai", "Gupta Paints, Kandivali East", 19.2064, 72.8687);
        userRepository.save(uPro8);
        ProfessionalProfile p8 = new ProfessionalProfile();
        p8.setUser(uPro8);
        p8.setCategory("PAINTER");
        p8.setCategoryTitle("Exterior Waterproofing & Texture Painter");
        p8.setBio("Certified master painter providing dustless mechanized sanding, waterproofing coatings, Italian stucco finishes, and durable exterior weather-defense coatings.");
        p8.setExperienceYears(10);
        p8.setQualifications("Master Painter Certified Applicator");
        p8.setSkills("Terrace Waterproofing, Royale Luxury Emulsion, Stencil Texture, Wall Putty & Sanding");
        p8.setMinimumPrice(18.0);
        p8.setPricingUnit(PricingUnit.PER_SQFT);
        p8.setServiceRadiusKm(25.0);
        p8.setAvailability(AvailabilityStatus.AVAILABLE);
        p8.setVerifiedByAdmin(true);
        p8.setAverageRating(4.6);
        p8.setReviewCount(22);
        p8.setAvatarUrl("https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150");
        p8.setYearsInArea(7);
        professionalProfileRepository.save(p8);

        // Pro 9: Builder / Contractor in Bengaluru
        User uPro9 = new User("karthik.builder@buildnear.com", passwordEncoder.encode("Pro@123"), "Karthik Reddy", "+91 99001 88223", Role.ROLE_PROFESSIONAL, "Bengaluru", "Reddy Infra, Whitefield", 12.9698, 77.7500);
        userRepository.save(uPro9);
        ProfessionalProfile p9 = new ProfessionalProfile();
        p9.setUser(uPro9);
        p9.setCategory("BUILDER_CONTRACTOR");
        p9.setCategoryTitle("Turnkey Home Construction Builder");
        p9.setBio("End-to-end residential home builders across Bengaluru. Complete handover with verified raw materials, live camera site feeds, and stringent QA checks.");
        p9.setExperienceYears(18);
        p9.setQualifications("B.E Civil, Registered Class-1 Contractor");
        p9.setSkills("Complete House Construction, Material Procurement, Structural Steel, Approvals");
        p9.setMinimumPrice(1750.0);
        p9.setPricingUnit(PricingUnit.PER_SQFT);
        p9.setServiceRadiusKm(50.0);
        p9.setAvailability(AvailabilityStatus.AVAILABLE);
        p9.setVerifiedByAdmin(true);
        p9.setAverageRating(4.9);
        p9.setReviewCount(15);
        p9.setAvatarUrl("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150");
        p9.setYearsInArea(14);
        professionalProfileRepository.save(p9);

        // Pro 10: Daily Wage Labor Lead
        User uPro10 = new User("ramesh.worker@buildnear.com", passwordEncoder.encode("Pro@123"), "Ramesh Yadav", "+91 98222 77110", Role.ROLE_PROFESSIONAL, "Mumbai", "Labor Naka Hub, Ghatkopar", 19.0860, 72.9090);
        userRepository.save(uPro10);
        ProfessionalProfile p10 = new ProfessionalProfile();
        p10.setUser(uPro10);
        p10.setCategory("DAILY_WAGE_WORKER");
        p10.setCategoryTitle("Skilled & Semi-Skilled Construction Labor Lead");
        p10.setBio("Reliable labor squad leader supplying 5 to 25 verified daily wage workers for excavation, concrete mixing, brick shifting, demolition debris clearing, and slab curing.");
        p10.setExperienceYears(8);
        p10.setQualifications("Skilled Site Crew Coordinator");
        p10.setSkills("Excavation, Concrete Mixing, Debris Clearing, Curing, Material Unloading");
        p10.setMinimumPrice(600.0);
        p10.setPricingUnit(PricingUnit.PER_DAY);
        p10.setServiceRadiusKm(20.0);
        p10.setAvailability(AvailabilityStatus.AVAILABLE);
        p10.setVerifiedByAdmin(false); // Unverified sample for admin testing!
        p10.setAverageRating(4.5);
        p10.setReviewCount(14);
        p10.setAvatarUrl("https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?w=150");
        p10.setYearsInArea(5);
        professionalProfileRepository.save(p10);

        // Pro 11: Welder & Fabricator
        User uPro11 = new User("iqbal.welder@buildnear.com", passwordEncoder.encode("Pro@123"), "Iqbal Khan", "+91 98112 55443", Role.ROLE_PROFESSIONAL, "Mumbai", "Khan Fabricators, Dharavi", 19.0434, 72.8567);
        userRepository.save(uPro11);
        ProfessionalProfile p11 = new ProfessionalProfile();
        p11.setUser(uPro11);
        p11.setCategory("WELDER");
        p11.setCategoryTitle("Structural Steel & Safety Grill Fabricator");
        p11.setBio("Custom iron gates, balcony safety grills, spiral staircases, tin/polycarbonate terrace sheds, and industrial beam welding with anti-corrosion primer coating.");
        p11.setExperienceYears(14);
        p11.setQualifications("ITI Metal Fabrication & Arc Welding");
        p11.setSkills("Arc & MIG Welding, MS Safety Grills, Iron Staircases, Polycarbonate Sheds");
        p11.setMinimumPrice(120.0);
        p11.setPricingUnit(PricingUnit.PER_SQFT);
        p11.setServiceRadiusKm(25.0);
        p11.setAvailability(AvailabilityStatus.AVAILABLE);
        p11.setVerifiedByAdmin(true);
        p11.setAverageRating(4.7);
        p11.setReviewCount(18);
        p11.setAvatarUrl("https://images.unsplash.com/photo-1501196354995-cbb51c65aaea?w=150");
        p11.setYearsInArea(10);
        professionalProfileRepository.save(p11);

        // Pro 12: Lineman & High Tension Technician
        User uPro12 = new User("ganesh.lineman@buildnear.com", passwordEncoder.encode("Pro@123"), "Ganesh Joshi", "+91 98901 33221", Role.ROLE_PROFESSIONAL, "Mumbai", "Joshi Power Works, Mulund West", 19.1726, 72.9565);
        userRepository.save(uPro12);
        ProfessionalProfile p12 = new ProfessionalProfile();
        p12.setUser(uPro12);
        p12.setCategory("LINEMAN");
        p12.setCategoryTitle("Power Line & Overhead Cable Specialist");
        p12.setBio("Certified Lineman for overhead street cables, HT transformer termination, backup generator tie-ins, and high-safety earthing pit construction.");
        p12.setExperienceYears(15);
        p12.setQualifications("State Electricity Board Authorized Lineman");
        p12.setSkills("Substation Connections, High-Voltage Lines, Pole Cabling, Transformer Maintenance");
        p12.setMinimumPrice(950.0);
        p12.setPricingUnit(PricingUnit.PER_DAY);
        p12.setServiceRadiusKm(35.0);
        p12.setAvailability(AvailabilityStatus.AVAILABLE);
        p12.setVerifiedByAdmin(true);
        p12.setAverageRating(4.8);
        p12.setReviewCount(16);
        p12.setAvatarUrl("https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?w=150");
        p12.setYearsInArea(9);
        professionalProfileRepository.save(p12);

        // 4. Portfolio Items
        PortfolioItem port1 = new PortfolioItem(p1, "Modern 4BHK Eco-Villa", "3,200 sq.ft cantilever design with solar rooftop and rainwater harvesting plan.", "House Construction", "Lonavala", "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=500", 2024);
        PortfolioItem port2 = new PortfolioItem(p1, "Duplex Penthouse 3D Blueprint", "Complete structural 3D elevation and municipal permit sanctioned set.", "House Construction", "Bandra West", "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=500", 2023);
        PortfolioItem port3 = new PortfolioItem(p2, "G+2 RCC Frame Residential Project", "End-to-end structural foundation, column casting, and slab testing.", "House Construction", "Thane", "https://images.unsplash.com/photo-1541888946425-d0fbb18615f3?w=500", 2024);
        PortfolioItem port4 = new PortfolioItem(p3, "Exposed Red Brick Heritage Bungalow", "Crafted with weather-proof mortar and precision pointed red bricks.", "House Construction", "Alibaug", "https://images.unsplash.com/photo-1590381105924-c72589b9ef3f?w=500", 2023);
        PortfolioItem port5 = new PortfolioItem(p4, "Minimalist Scandinavian 3BHK", "Custom acrylic modular kitchen, concealed warm cove lighting, and oak finish wardrobes.", "Interior Design", "Worli", "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=500", 2024);
        PortfolioItem port6 = new PortfolioItem(p4, "Emerald Modular Island Kitchen", "Quartz countertops with tandem Hafele soft-close drawers and chimney installation.", "Interior Design", "Juhu", "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=500", 2024);
        PortfolioItem port7 = new PortfolioItem(p5, "Complete 3-Phase Apartment Rewiring", "Concealed FR-LSH wiring with Schneider MCB distribution and smart switch panels.", "Electrical Work", "Andheri", "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=500", 2024);
        PortfolioItem port8 = new PortfolioItem(p6, "Concealed Grohe Bathroom Plumbing", "Concealed diverters, high pressure CPVC lines, and acoustic drainage pipe setup.", "Plumbing Work", "Powai", "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=500", 2023);
        portfolioItemRepository.saveAll(Arrays.asList(port1, port2, port3, port4, port5, port6, port7, port8));

        // 5. Service Packages (House Construction & Interior Design Modules)
        ServicePackage sp1 = new ServicePackage();
        sp1.setCategory("HOUSE_CONSTRUCTION");
        sp1.setPackageCode("HC_BASIC");
        sp1.setTitle("Turnkey Basic Standard House Construction");
        sp1.setShortDescription("Quality budget home construction with Fe500 steel and 53-grade cement.");
        sp1.setDetailedDescription("Ideal for budget residential houses. Includes soil test, excavation, RCC column frame, red brick masonry, plastering, ceramic floor tiles, and standard sanitary fixtures.");
        sp1.setFeatures("RCC Frame Structure||Fe500 Grade TMT Steel||ACC / UltraTech Cement||Ceramic Flooring Tiles||Concealed Copper Wiring||Overhead Sintex Water Tank||10-Year Structural Warranty");
        sp1.setStartingPrice(1650.0);
        sp1.setPricingUnit("sq.ft");
        sp1.setEstimatedDurationDays(180);
        sp1.setPopular(false);
        sp1.setBadgeText("Budget Friendly");
        sp1.setIconName("Home");
        sp1.setImageUrl("https://images.unsplash.com/photo-1590381105924-c72589b9ef3f?w=600");

        ServicePackage sp2 = new ServicePackage();
        sp2.setCategory("HOUSE_CONSTRUCTION");
        sp2.setPackageCode("HC_PREMIUM");
        sp2.setTitle("Turnkey Premium Modern Villa Construction");
        sp2.setShortDescription("Premium grade construction with branded fixtures, vitrified tiles & teakwood doors.");
        sp2.setDetailedDescription("Complete residential construction package using Tata Tiscon steel, AAC/Red brick walls, double-coat external waterproofing, Vitrified 4x2 tiles, Jaquar bath fittings, and automated wiring.");
        sp2.setFeatures("Tata Tiscon Fe500D Primary Steel||UltraTech Weather Plus Cement||800x1200mm Glazed Vitrified Tiles||Jaquar Bath Fittings & Diverters||Teakwood Main Door Frame||Waterproofing on all wet areas & terrace||10-Year Structural Guarantee");
        sp2.setStartingPrice(2150.0);
        sp2.setPricingUnit("sq.ft");
        sp2.setEstimatedDurationDays(210);
        sp2.setPopular(true);
        sp2.setBadgeText("Most Popular");
        sp2.setIconName("Sparkles");
        sp2.setImageUrl("https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=600");

        ServicePackage sp3 = new ServicePackage();
        sp3.setCategory("HOUSE_CONSTRUCTION");
        sp3.setPackageCode("HC_LUXURY");
        sp3.setTitle("Luxury Architectural Villa Construction");
        sp3.setShortDescription("Top-of-the-line architectural construction with imported marble & smart home automation.");
        sp3.setDetailedDescription("Curated for luxury villas. Features cantilever architectural elements, imported Italian marble, Kohler / Grohe fittings, high-efficiency thermal brickwork, and EV charger readiness.");
        sp3.setFeatures("Custom Architectural 3D Elevation||Imported Italian Marble in Living||Kohler / Grohe Concealed Bathware||Double Glazed Soundproof Windows||Smart Home Electrical Automation||15-Year Structural Warranty");
        sp3.setStartingPrice(2850.0);
        sp3.setPricingUnit("sq.ft");
        sp3.setEstimatedDurationDays(270);
        sp3.setPopular(false);
        sp3.setBadgeText("Luxury Grade");
        sp3.setIconName("Crown");
        sp3.setImageUrl("https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=600");

        ServicePackage sp4 = new ServicePackage();
        sp4.setCategory("HOUSE_CONSTRUCTION");
        sp4.setPackageCode("HC_ARCH_PLAN");
        sp4.setTitle("Architectural Blueprint & 3D Structural Plan");
        sp4.setShortDescription("Complete 2D floor plans, 3D photorealistic elevation, and municipal submission drawings.");
        sp4.setDetailedDescription("Comprehensive architectural package including Vastu-compliant room layouts, structural column placement, plumbing/electrical schematic drawings, and high-resolution 3D daylight/night views.");
        sp4.setFeatures("2D Detailed Floor Plans||3D Photorealistic Exterior Views||Structural Column & Beam Schedule||Plumbing & Electrical Circuit Schematics||Municipal Sanction Blueprint File");
        sp4.setStartingPrice(15.0);
        sp4.setPricingUnit("sq.ft");
        sp4.setEstimatedDurationDays(14);
        sp4.setPopular(false);
        sp4.setBadgeText("Essential");
        sp4.setIconName("DraftingCompass");
        sp4.setImageUrl("https://images.unsplash.com/photo-1503387762-592deb58ef4e?w=600");

        ServicePackage sp5 = new ServicePackage();
        sp5.setCategory("INTERIOR_DESIGN");
        sp5.setPackageCode("INT_MODULAR_KITCHEN");
        sp5.setTitle("Modular Kitchen Design & Turnkey Installation");
        sp5.setShortDescription("Anti-termite BWP marine plywood kitchen with acrylic/laminate finish & soft-close drawers.");
        sp5.setDetailedDescription("Custom built to fit your kitchen footprint (L-shaped, Parallel, U-shaped, Island). Includes tandem drawer boxes, spice pullouts, cutlery organizers, and quartz/granite countertop integration.");
        sp5.setFeatures("BWP Marine Plywood IS:710 Grade||Anti-Bubble Acrylic / Laminate Finish||Hafele / Hettich Soft-Close Hinges||SS304 Rustproof Baskets & Organizers||Chimney & Hob Integration||10-Year Plywood Warranty");
        sp5.setStartingPrice(145000.0);
        sp5.setPricingUnit("project");
        sp5.setEstimatedDurationDays(21);
        sp5.setPopular(true);
        sp5.setBadgeText("Best Seller");
        sp5.setIconName("CookingPot");
        sp5.setImageUrl("https://images.unsplash.com/photo-1556911220-e15b29be8c8f?w=600");

        ServicePackage sp6 = new ServicePackage();
        sp6.setCategory("INTERIOR_DESIGN");
        sp6.setPackageCode("INT_2BHK_FULL");
        sp6.setTitle("Complete 2 BHK Turnkey Interior Package");
        sp6.setShortDescription("Full interior makeover including kitchen, 2 bedroom wardrobes, false ceiling & paint.");
        sp6.setDetailedDescription("Move-in ready turnkey package. Covers living room entertainment unit, designer false ceiling with COB lighting, modular kitchen, master and guest bedroom wardrobes, and premium wall finishes.");
        sp6.setFeatures("Complete Modular Kitchen with Accessories||2 Floor-to-Ceiling Wardrobes with Lofts||Gyproc False Ceiling with LED Profile||Asian Paints Royale Luxury Emulsion||Designer TV Wall Unit with Louvers||10-Year Flat Warranty");
        sp6.setStartingPrice(420000.0);
        sp6.setPricingUnit("project");
        sp6.setEstimatedDurationDays(40);
        sp6.setPopular(true);
        sp6.setBadgeText("Most Value");
        sp6.setIconName("BedDouble");
        sp6.setImageUrl("https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=600");

        ServicePackage sp7 = new ServicePackage();
        sp7.setCategory("INTERIOR_DESIGN");
        sp7.setPackageCode("INT_CEILING_LIGHT");
        sp7.setTitle("False Ceiling & Designer Architectural Lighting");
        sp7.setShortDescription("Saint-Gobain Gyproc ceiling with warm dimmable LED cove lighting & magnetic track lights.");
        sp7.setDetailedDescription("Precision suspended false ceiling tailored to your room height. Clean concealed wiring, peripheral cove illumination, and accent spotlights.");
        sp7.setFeatures("Saint-Gobain Gyproc Gypsum Boards||Anti-Rust Galvanized Steel Framing||Concealed LED Strip Cove Lighting||Spotlights & Magnetic Track Provision||Crack-Free Joint Tape & Smooth Putty");
        sp7.setStartingPrice(85.0);
        sp7.setPricingUnit("sq.ft");
        sp7.setEstimatedDurationDays(8);
        sp7.setPopular(false);
        sp7.setBadgeText("Fast Turnaround");
        sp7.setIconName("Lightbulb");
        sp7.setImageUrl("https://images.unsplash.com/photo-1513694203232-719a280e022f?w=600");

        servicePackageRepository.saveAll(Arrays.asList(sp1, sp2, sp3, sp4, sp5, sp6, sp7));

        // 6. Sample Service Request (with Quotations for comparison)
        ServiceRequest req1 = new ServiceRequest();
        req1.setCustomer(customer1);
        req1.setCategory("CIVIL_ENGINEER");
        req1.setTitle("G+1 Independent House RCC Slab & Column Construction");
        req1.setDescription("Looking for experienced structural engineer/contractor to oversee foundation excavation, 14 RCC column casting, and first floor slab casting for 1,800 sq.ft plot.");
        req1.setPropertyType("Residential Plot");
        req1.setAreaSqFt(1800.0);
        req1.setEstimatedBudget(3200000.0);
        req1.setAddress("Plot 24, Sai Shraddha Enclave, Andheri West");
        req1.setCity("Mumbai");
        req1.setLatitude(19.1150);
        req1.setLongitude(72.8710);
        req1.setPreferredStartDate(LocalDate.now().plusDays(10));
        req1.setUrgency("WITHIN_WEEK");
        req1.setStatus(RequestStatus.QUOTED);
        req1.setCreatedAt(LocalDateTime.now().minusDays(3));
        ServiceRequest savedReq1 = serviceRequestRepository.save(req1);

        // Multiple Quotations for this request (for Side-by-Side Comparison feature!)
        Quotation q1 = new Quotation();
        q1.setServiceRequest(savedReq1);
        q1.setProfessional(p2); // Er. Suresh Patel
        q1.setTotalAmount(3150000.0);
        q1.setLaborCost(1100000.0);
        q1.setMaterialCost(2050000.0);
        q1.setEstimatedDays(120);
        q1.setBreakdownNotes("Includes M25 Grade Ready Mix Concrete, Tata Tiscon Fe500D steel, machine vibrating, cube test reports, and 21 days automated curing.");
        q1.setTermsAndWarranty("10-Year Structural Guarantee with quarterly free site inspection in Year 1.");
        q1.setStatus(QuotationStatus.PENDING);
        q1.setCreatedAt(LocalDateTime.now().minusDays(2));

        Quotation q2 = new Quotation();
        q2.setServiceRequest(savedReq1);
        q2.setProfessional(p9); // Karthik Builder
        q2.setTotalAmount(3350000.0);
        q2.setLaborCost(1200000.0);
        q2.setMaterialCost(2150000.0);
        q2.setEstimatedDays(105);
        q2.setBreakdownNotes("Turnkey materials package with Jindal Panther TMT, UltraTech Weather Plus cement, digital ultrasonic pulse velocity test, and live CCTV stream for owner.");
        q2.setTermsAndWarranty("15-Year Structural Guarantee + Insurance coverage during active construction.");
        q2.setStatus(QuotationStatus.PENDING);
        q2.setCreatedAt(LocalDateTime.now().minusDays(1));

        quotationRepository.saveAll(Arrays.asList(q1, q2));

        // 7. Sample Bookings & Reviews
        // Booking 1: Completed with review
        Booking b1 = new Booking();
        b1.setBookingNumber("BN-2026-101");
        b1.setCustomer(customer1);
        b1.setProfessional(p3); // Rajesh Mason
        b1.setServiceTitle("AAC Block Wall Construction & Sand-Face Plastering");
        b1.setServiceCategory("MASON");
        b1.setAgreedPrice(34000.0);
        b1.setPricingUnit("PER_DAY");
        b1.setBookingDate(LocalDate.now().minusDays(20));
        b1.setScheduledDate(LocalDate.now().minusDays(18));
        b1.setStatus(BookingStatus.COMPLETED);
        b1.setCustomerAddress("A-402, Sea Green Apartments, Andheri West");
        b1.setCustomerPhone("+91 98765 43210");
        b1.setCustomerNotes("Please bring plumb lines and spirit levels for straight wall alignment.");
        b1.setCompletionNotes("Wall completed smoothly with 40-day curing instructions provided.");
        b1.setCreatedAt(LocalDateTime.now().minusDays(20));
        b1.setUpdatedAt(LocalDateTime.now().minusDays(10));
        Booking savedB1 = bookingRepository.save(b1);

        Review r1 = new Review(savedB1, customer1, p3, 5, "Rajesh and his team did an outstanding job with the AAC block work. Completely straight lines, neat mortar joints, and finished 1 day ahead of schedule. Highly recommended!");
        r1.setProfessionalResponse("Thank you so much Aditi ma'am! It was a pleasure working on your home.");
        reviewRepository.save(r1);

        // Booking 2: Active / In Progress
        Booking b2 = new Booking();
        b2.setBookingNumber("BN-2026-102");
        b2.setCustomer(customer1);
        b2.setProfessional(p5); // Vikram Electrician
        b2.setServiceTitle("Full Apartment Concealed Conduit Wiring & Earthing");
        b2.setServiceCategory("ELECTRICIAN");
        b2.setAgreedPrice(18500.0);
        b2.setPricingUnit("PER_DAY");
        b2.setBookingDate(LocalDate.now().minusDays(4));
        b2.setScheduledDate(LocalDate.now().plusDays(1));
        b2.setStatus(BookingStatus.IN_PROGRESS);
        b2.setCustomerAddress("A-402, Sea Green Apartments, Andheri West");
        b2.setCustomerPhone("+91 98765 43210");
        b2.setCustomerNotes("Requires 3-phase DB installation and AC line circuit breakers.");
        b2.setCreatedAt(LocalDateTime.now().minusDays(4));
        b2.setUpdatedAt(LocalDateTime.now().minusDays(1));
        bookingRepository.save(b2);

        // Booking 3: Confirmed / Scheduled
        Booking b3 = new Booking();
        b3.setBookingNumber("BN-2026-103");
        b3.setCustomer(customer2);
        b3.setProfessional(p4); // Ananya Interior
        b3.setServiceTitle("2 BHK Modular Kitchen 3D Rendering & Site Consultation");
        b3.setServiceCategory("INTERIOR_DESIGNER");
        b3.setAgreedPrice(5000.0);
        b3.setPricingUnit("PER_PROJECT");
        b3.setBookingDate(LocalDate.now().minusDays(1));
        b3.setScheduledDate(LocalDate.now().plusDays(3));
        b3.setStatus(BookingStatus.CONFIRMED);
        b3.setCustomerAddress("124, 4th Cross, Indiranagar, Bengaluru");
        b3.setCustomerPhone("+91 98111 22334");
        b3.setCustomerNotes("Please bring laminate material catalogs and quartz samples.");
        b3.setCreatedAt(LocalDateTime.now().minusDays(1));
        b3.setUpdatedAt(LocalDateTime.now().minusDays(1));
        bookingRepository.save(b3);

        // Seed Nationwide Specialists across all state hubs
        seedNationwideProfessionals();

        logger.info("BuildNear sample data successfully loaded!");
        logger.info("Demo Accounts ready:");
        logger.info(" - Customer: customer@buildnear.com / Customer@123");
        logger.info(" - Professional: rajesh.mason@buildnear.com / Pro@123");
        logger.info(" - Admin: admin@buildnear.com / Admin@123");
    }

    private void seedNationwideProfessionals() {
        Object[][] cities = new Object[][] {
            // City, State, Lat, Lng
            {"Bengaluru", "Karnataka", 12.9716, 77.5946},
            {"Pune", "Maharashtra", 18.5204, 73.8567},
            {"Delhi NCR", "Delhi", 28.6139, 77.2090},
            {"Hyderabad", "Telangana", 17.3850, 78.4867},
            {"Chennai", "Tamil Nadu", 13.0827, 80.2707},
            {"Kolkata", "West Bengal", 22.5726, 88.3639},
            {"Ahmedabad", "Gujarat", 23.0225, 72.5714},
            {"Jaipur", "Rajasthan", 26.9124, 75.7873},
            {"Lucknow", "Uttar Pradesh", 26.8467, 80.9462},
            {"Chandigarh", "Punjab / Haryana", 30.7333, 76.7794},
            {"Kochi", "Kerala", 9.9312, 76.2673},
            {"Bhopal", "Madhya Pradesh", 23.2599, 77.4126},
            {"Patna", "Bihar", 25.5941, 85.1376},
            {"Bhubaneswar", "Odisha", 20.2961, 85.8245},
            {"Guwahati", "Assam", 26.1445, 91.7362},
            {"Goa (Panaji)", "Goa", 15.4909, 73.8278},
            {"Dehradun", "Uttarakhand", 30.3165, 78.0322},
            {"Indore", "Madhya Pradesh", 22.7196, 75.8577},
            {"Visakhapatnam", "Andhra Pradesh", 17.6868, 83.2185},
            {"Ranchi", "Jharkhand", 23.3441, 85.3096},
            {"Raipur", "Chhattisgarh", 21.2514, 81.6296},
            {"Nagpur", "Maharashtra", 21.1458, 79.0882},
            {"Surat", "Gujarat", 21.1702, 72.8311},
            {"Coimbatore", "Tamil Nadu", 11.0168, 76.9558}
        };

        Object[][] trades = new Object[][] {
            {"ARCHITECT", "Architectural Consultant & 3D Visualizer", "Architectural Council Certified", "AutoCAD, BIM, Vastu 3D, Elevation Renderings", 2200.0, PricingUnit.PER_PROJECT, "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150"},
            {"CIVIL_ENGINEER", "Structural & Soil Foundation Engineer", "M.Tech Structural Engineering", "RCC Framework, Column Alignment, Soil Test, Slab Supervision", 1950.0, PricingUnit.PER_SQFT, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150"},
            {"BUILDER_CONTRACTOR", "Turnkey Residential Builder", "Certified Master Contractor Grade A", "End-to-End Home Construction, Material Procurement, Site Crew", 2100.0, PricingUnit.PER_SQFT, "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150"},
            {"MASON", "Master Mason & Brickwork Lead", "Govt CSDCI Certified Lead Mason", "Red Clay Bricks, AAC Blocks, Plastering, Granite Fitting", 850.0, PricingUnit.PER_DAY, "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150"},
            {"INTERIOR_DESIGNER", "Contemporary Interior Specialist", "Diploma Interior Architecture", "Modular Kitchens, Wardrobes, False Ceiling, Italian Lighting", 1500.0, PricingUnit.PER_PROJECT, "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150"},
            {"ELECTRICIAN", "Licensed 3-Phase Electrician", "Govt Wireman License Grade-1", "Concealed Conduit, DB Distribution, MCB Inverters, Earthing", 550.0, PricingUnit.PER_DAY, "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150"},
            {"PLUMBER", "Sanitary & Drainage Plumber", "ITI Certified Master Plumber", "CPVC/UPVC Piping, Overhead Tank, Bathroom Fixtures, Drainage", 450.0, PricingUnit.PER_DAY, "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150"},
            {"CARPENTER", "Modular Wood & Cabinet Artisan", "Master Wood Craftsman Certificate", "Marine Plywood, Acrylic Laminate Pressing, Hydraulic Beds, Hettich & Hafele Hardware, Sliding Wardrobes", 800.0, PricingUnit.PER_DAY, "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150"},
            {"PAINTER", "Exterior Waterproofing & Texture Painter", "Certified Asian Paints Applicator", "Apex Ultima, Royale Luxury, Terrace Waterproofing, Stucco", 18.0, PricingUnit.PER_SQFT, "https://images.unsplash.com/photo-1501196354995-cbb51c65aaea?w=150"},
            {"DAILY_WAGE_WORKER", "Skilled Construction Labor Squad", "Certified Site Crew Supervisor", "Excavation, Concrete Pouring, Debris Clearance, Slab Curing", 600.0, PricingUnit.PER_DAY, "https://images.unsplash.com/photo-1547425260-76bcadfb4f2c?w=150"},
            {"WELDER", "Structural Steel & Safety Grill Fabricator", "ITI Arc & MIG Welding Certified", "MS Safety Grills, Balcony Railings, Terrace Polycarbonate Sheds", 130.0, PricingUnit.PER_SQFT, "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150"},
            {"LINEMAN", "Heavy High Voltage Lineman", "Govt Electrical Inspectorate Certified", "Overhead Service Cable, 3-Phase Drop Line, Industrial Phase Shift", 900.0, PricingUnit.PER_DAY, "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150"}
        };

        String[] firstNames = {"Amit", "Rohan", "Suresh", "Manish", "Praveen", "Kiran", "Deepak", "Anil", "Sunil", "Vivek", "Dinesh", "Gopal"};
        String[] lastNames = {"Sharma", "Patel", "Reddy", "Rao", "Nair", "Ghosh", "Joshi", "Verma", "Singh", "Yadav", "Chauhan", "Das"};

        for (int cIdx = 0; cIdx < cities.length; cIdx++) {
            String cityName = (String) cities[cIdx][0];
            String region = (String) cities[cIdx][1];
            double baseLat = (Double) cities[cIdx][2];
            double baseLng = (Double) cities[cIdx][3];

            for (int tIdx = 0; tIdx < trades.length; tIdx++) {
                String tradeCategory = (String) trades[tIdx][0];
                String tradeTitle = (String) trades[tIdx][1];
                String qual = (String) trades[tIdx][2];
                String skills = (String) trades[tIdx][3];
                Double minPrice = (Double) trades[tIdx][4];
                PricingUnit unit = (PricingUnit) trades[tIdx][5];
                String avatar = (String) trades[tIdx][6];

                String fName = firstNames[(cIdx + tIdx) % firstNames.length];
                String lName = lastNames[(cIdx * 2 + tIdx) % lastNames.length];
                String fullName = fName + " " + lName;
                String email = (fName + "." + tradeCategory.toLowerCase() + "." + cityName.replaceAll("[^a-zA-Z]", "").toLowerCase() + "@buildnear.com");

                if (userRepository.findByEmail(email).isPresent()) {
                    continue;
                }

                // Disperse coordinates slightly within city radius
                double offsetLat = ((tIdx % 5) - 2) * 0.015;
                double offsetLng = (((tIdx * 3) % 5) - 2) * 0.015;

                User u = new User(
                    email,
                    passwordEncoder.encode("Pro@123"),
                    fullName,
                    "+91 9" + (800000000L + (cIdx * 10000L) + tIdx),
                    Role.ROLE_PROFESSIONAL,
                    cityName,
                    "Central Hub, " + cityName + " (" + region + ")",
                    baseLat + offsetLat,
                    baseLng + offsetLng
                );
                userRepository.save(u);

                ProfessionalProfile p = new ProfessionalProfile();
                p.setUser(u);
                p.setCategory(tradeCategory);
                p.setCategoryTitle(tradeTitle);
                p.setBio("Experienced " + tradeTitle.toLowerCase() + " serving the " + cityName + " metro area with verified workmanship standards and on-time delivery guarantee.");
                p.setExperienceYears(6 + ((cIdx + tIdx) % 15));
                p.setQualifications(qual);
                p.setSkills(skills);
                p.setMinimumPrice(minPrice);
                p.setPricingUnit(unit);
                p.setServiceRadiusKm(35.0);
                p.setAvailability(AvailabilityStatus.AVAILABLE);
                p.setVerifiedByAdmin(true);
                p.setAverageRating(4.5 + (((cIdx + tIdx) % 5) * 0.1));
                p.setReviewCount(12 + ((cIdx * 3 + tIdx) % 30));
                p.setAvatarUrl(avatar);
                p.setYearsInArea(5 + (cIdx % 10));

                professionalProfileRepository.save(p);
            }
        }
    }
}
