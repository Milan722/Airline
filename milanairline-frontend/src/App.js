import { BrowserRouter,Navigate, Route, Routes } from "react-router-dom";
import Navbar from "./components/common/Navbar";
import Footer from "./components/common/Footer";
import RegisterPage from "./components/auth/RegisterPage";
import LoginPage from "./components/auth/LoginPage";
import HomePage from "./components/pages/HomePage";
import FindLightsPage from "./components/pages/FindFlightsPage";
import ProfilePage from "./components/profile/ProfilePage";
import UpdateProfilePage from "./components/profile/UpdateProfilePage";
import BookingPage from "./components/pages/BookingPage";
import BookingDetailsPage from "./components/pages/BookingDetailsPage";
import {RouteGuard} from "./services/RouteGuard";
import AdminDashboardPage from "./components/admin/AdminDashboardPage";
import AdminBookingDetailsPage from "./components/admin/AdminBookingDetailsPage";
import AdminFlightDetailPage from "./components/admin/AdminFlightDetailPage";
import AddEditAirportPage from "./components/admin/AddEditAirportPage";
import AddFlightPage from "./components/admin/AddFlightPage";
import SpecialRegistration from "./components/admin/SpecialRegistration";


function App(){
  return (
    <BrowserRouter>
    <Navbar/>
    <div className="content">
      <Routes>
        <Route path="/register" element={<RegisterPage/>}/>
        <Route path="/login" element={<LoginPage/>}/>

        <Route path="/home" element={<HomePage/>}/>
        <Route path="/flights" element={<FindLightsPage/>}/>


        
        <Route path="/profile" element={<RouteGuard allowedRoles={["CUSTOMER"]} element={<ProfilePage/>}/>}/>     
        <Route path="/update-profile" element={<RouteGuard allowedRoles={["CUSTOMER"]} element={<UpdateProfilePage/>}/>}/>
        
        <Route path="/book-flight/:id" element={<RouteGuard allowedRoles={["CUSTOMER","ADMIN","PILOT"]} element={<BookingPage/>}/>}/>
        <Route path="/booking/:id" element={<RouteGuard allowedRoles={["CUSTOMER","ADMIN","PILOT"]}element={<BookingDetailsPage/>}/>}/>

         <Route path="/admin" element={<RouteGuard allowedRoles={["ADMIN","PILOT"]}element={<AdminDashboardPage/>}/>}/>
         <Route path="/admin/booking/:id" element={<RouteGuard allowedRoles={["ADMIN","PILOT"]}element={<AdminBookingDetailsPage/>}/>}/>
         <Route path="/admin/flight/:id" element={<RouteGuard allowedRoles={["PILOT"]}element={<AdminFlightDetailPage/>}/>}/>

         <Route path="/add-airport" element={<RouteGuard allowedRoles={["ADMIN"]}element={<AddEditAirportPage/>}/>}/>
         <Route path="/edit-airport/:id" element={<RouteGuard allowedRoles={["ADMIN"]}element={<AddEditAirportPage/>}/>}/>
          <Route path="/add-flight" element={<RouteGuard allowedRoles={["ADMIN","PILOT"]}element={<AddFlightPage/>}/>}/>
        <Route path="/special-register" element={<RouteGuard allowedRoles={["ADMIN"]}element={<SpecialRegistration/>}/>}/>



        




        <Route path="*" element={<Navigate to="/home"/>}/>
      </Routes>

    </div>


<Footer/>
    </BrowserRouter>
  )


}
export default App;