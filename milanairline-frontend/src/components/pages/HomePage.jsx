import { useNavigate, Link } from "react-router-dom";
import { useMessage } from "../common/MessageDisplay";
import { useEffect, useState } from "react";
import ApiService from "../../services/ApiService";

const HomePage = () => {
    const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const navigate = useNavigate();
    const [airports, setAirports] = useState([]);
   


    const [searchData, setSearchData] = useState({
        departureIataCode: "",
        arrivalIataCode: "",
        departureDate: ""
    })

    const popularDestinations = [
        { id: 1, city: "New York", country: "USA", price: "$450", image: "usa.jpg" },
        { id: 1, city: "London", country: "UK", price: "$350", image: "uk.jpg" },
        { id: 1, city: "Dubai", country: "UAE", price: "$600", image: "uae.webp" },
        { id: 1, city: "Tokyo", country: "Japan", price: "$1300", image: "japan.webp" }
    ]


    useEffect(() => {
        const fetchAirports = async () => {
            try {
                const response = await ApiService.getAllAirports();
                setAirports(response.data || []);
            } catch (error) {
                showError("Failed to load airports");

            }
        }
        fetchAirports();
    }, []);

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchData.departureIataCode || !searchData.arrivalIataCode || !searchData.departureDate) {
            showError("Please select departure, arrival airports and dates")
            return;
        }
        navigate(`/flights?departureIataCode=${searchData.departureIataCode}&arrivalIataCode=${searchData.arrivalIataCode}&departureDate=${searchData.departureDate}`);
    }
    const handleSwapAirports = () => {
        setSearchData({
            ...searchData,
            departureIataCode: searchData.arrivalIataCode,
            arrivalIataCode: searchData.departureIataCode
        })
    }

    const formatAirportOption = (airport) => {
        return `${airport.iataCode}(${airport.city}) - ${airport.name}`;
    }

    return (
        <div className="home-page">
            <div className="hero-section">
                <div className="hero-content">
                    <h1>Book Your Flight with Milan Airlines</h1>
                    <p>Find the best deals for your journey</p>
                </div>
                <div className="search-box">
                    <ErrorDisplay />
                    <SuccessDisplay />

                    <form onSubmit={handleSearch}>
                        <div className="search-fields">

                            <div className="form-group">
                                <label htmlFor="">From</label>
                                <select
                                required
                                    value={searchData.departureIataCode}
                                    onChange={(e) => setSearchData({ ...searchData, departureIataCode: e.target.value })}>
                                    <option value="">
                                        Select Departure Airport
                                    </option>
                                    {airports.map(airport => (
                                        <option key={`dep-${airport.iataCode}`} value={airport.iataCode}>
                                            {formatAirportOption(airport)}</option>
                                    ))}
                                </select>

                            </div>
                            <div className="swap-cities">
                                <button type="button"
                                    onClick={handleSwapAirports}
                                    aria-label="Swap Departure and Arrival">
                                    ↔
                                </button>
                            </div>
                            <div className="form-group">
                                <label htmlFor="">To</label>
                                <select
                                    value={searchData.arrivalIataCode}
                                    onChange={(e) => setSearchData({ ...searchData, arrivalIataCode: e.target.value })}>
                                    <option value="">
                                        Select Arrival Airport
                                    </option>
                                    {airports.filter(airport => airport.iataCode !== searchData.departureIataCode)
                                        .map(airport => (
                                            <option key={`arr-${airport.iataCode}`} value={airport.iataCode}>
                                                {formatAirportOption(airport)}
                                            </option>
                                        ))}
                                </select>

                            </div>
                            <div className="form-group">
                                <label htmlFor="">Departure Date</label>
                                <input type="date"
                                    required
                                    value={searchData.departureDate}
                                    onChange={(e) => setSearchData({ ...searchData, departureDate: e.target.value })}
                                    min={new Date().toISOString().split('T')[0]} />

                            </div>


                        </div>
                        <button className="search-button" type="submit">
                            Search Flights
                        </button>

                    </form>
                </div>
            </div>
            {/* Popular Destinations */}
            <section className="popular-destinations">
                <h2>Popular Destinations</h2>
                <p>Explore our most booked flight routes</p>

                <div className="destinations-grid">
                    {popularDestinations.map(destination => (
                        <div key={destination.id} className="destination-card">
                            <div className="destination-image" style={{ backgroundImage: `url(/images/${destination.image})` }}>
                                <div className="destination-overlay">
                                    <h3>{destination.city}</h3>
                                    <p>{destination.country}</p>
                                </div>
                            </div>
                            <div className="destination-footer">
                                <span>From {destination.price}</span>
                                <Link to="/flights" className="book-button">Book Now</Link>
                            </div>
                        </div>
                    ))}
                </div>
            </section>
             {/* Why Choose Us */}
            <section className="features-section">
                <h2>Why Choose Milan Airlines?</h2>

                <div className="features-grid">
                    <div className="feature-card">
                        <div className="feature-icon">✈️</div>
                        <h3>Modern Fleet</h3>
                        <p>Fly in comfort with our state-of-the-art aircraft featuring the latest amenities.</p>
                    </div>

                    <div className="feature-card">
                        <div className="feature-icon">🕒</div>
                        <h3>On-Time Performance</h3>
                        <p>We pride ourselves on our industry-leading punctuality record.</p>
                    </div>

                    <div className="feature-card">
                        <div className="feature-icon">🍽️</div>
                        <h3>Gourmet Dining</h3>
                        <p>Enjoy chef-curated meals inspired by global cuisines.</p>
                    </div>

                    <div className="feature-card">
                        <div className="feature-icon">💺</div>
                        <h3>Extra Legroom</h3>
                        <p>More space to relax with our generous seat pitch in all classes.</p>
                    </div>
                </div>
            </section>

            {/* Special Offers */}
            <section className="offers-section">
                <h2>Special Offers</h2>
                <p>Don't miss these exclusive deals</p>

                <div className="offer-card">
                    <div className="offer-content">
                        <h3>Summer Sale - Up to 30% Off!</h3>
                        <p>Book by June 30 for travel between July and September.</p>
                        <Link to="/flights" className="offer-button">View Deals</Link>
                    </div>
                </div>
            </section>
            {/* Testimonials */}
            <section className="testimonials-section">
                <h2>What Our Passengers Say</h2>

                <div className="testimonials-grid">
                    <div className="testimonial-card">
                        <div className="testimonial-text">
                            "The service was exceptional from booking to landing. Will definitely fly with MilanAirlines again!"
                        </div>
                        <div className="testimonial-author">
                            <div className="author-name">Sarah Johnson</div>
                            <div className="author-detail">Frequent Flyer</div>
                        </div>
                    </div>

                    <div className="testimonial-card">
                        <div className="testimonial-text">
                            "Most comfortable economy seats I've experienced. The crew made the long flight enjoyable."
                        </div>
                        <div className="testimonial-author">
                            <div className="author-name">Michael Chen</div>
                            <div className="author-detail">Business Traveler</div>
                        </div>
                    </div>
                </div>
            </section>

        </div>
    )



}
export default HomePage
