import { Link ,useNavigate} from "react-router-dom";
import { useMessage } from "../common/MessageDisplay";
import { useEffect, useState } from "react";
import ApiService from "../../services/ApiService";



const UpdateProfilePage=()=>{
     const { ErrorDisplay, SuccessDisplay, showError, showSuccess } = useMessage();
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

     const [user, setUser] = useState({
        name: "",
        phoneNumber: "",
        password: "",
        confirmPassword: ""
    });

    useEffect(() => {
        fetchUserProfile();
    }, []);

    const fetchUserProfile=async()=>{
        try {
            const response=await ApiService.getAccountDetails();

            setUser(prev => ({
                ...prev,
                name: response.data.name,
                phoneNumber: response.data.phoneNumber || ""
            }));
            
        } catch (error) {
            showError(error.response?.data?.message || "Failed to fetch profile");
            
        }finally{
            setLoading(false);
        }
    }
    const handleChange = (e) => {
        const { name, value } = e.target

        setUser(prev => (
            {
                ...prev, [name]: value
            }
        ))}
            const handleSubmit = async (e) => {

        e.preventDefault();

        console.log("handleSubmit called")

        // Full name regex validation - two words, each starting with capital letter
        const fullNameRegex = /^[A-ZČĆŽŠĐ][a-zčćžšđ]+ [A-ZČĆŽŠĐ][a-zčćžšđ]+$/;
        if(!fullNameRegex.test(user.name)){
            showError("Full name must contain exactly two words, each starting with a capital letter")
            return;
        }

        // Phone number validation - must start with + and contain only numbers after + (only if provided)
        if(user.phoneNumber){
            const phoneRegex = /^\+[0-9]+$/;
            if(!phoneRegex.test(user.phoneNumber)){
                showError("Phone number must start with + and contain only numbers")
                return;
            }
        }

        // Password validation (only if password is provided)
        if(user.password){
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/;
            if(!passwordRegex.test(user.password)){
                showError("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number")
                return;
            }

            if(user.password !== user.confirmPassword){
                showError("Password do not match")
                return;
            }
        }

        try {
            const requestBody = {
                name: user.name,
                phoneNumber: user.phoneNumber || null,
                password: user.password || undefined,

            }

            const isToUpdate = window.confirm("Are you sure you want to update your account?");

            if (!isToUpdate) return;

            const resp = await ApiService.updateMyAccount(requestBody);

            if (resp.statusCode === 200) {
                showSuccess("Account updated successfully!");
                navigate("/profile");
            }

        } catch (error) {
            showError(error.response?.data?.message || "Failed to update profile");

        }
    }


    if (loading) return <div className="update-profile-loading">Loading Profile</div>

    
    return (
        <div className="update-profile-container">
            <div className="update-profile-card">
                <ErrorDisplay />
                <SuccessDisplay />

                <h2 className="update-profile-title">Update Profile</h2>


                <form onSubmit={handleSubmit} className="update-profile-form">
                    <div className="update-profile-form-group">
                        <label htmlFor="name" className="update-profile-label">
                            Full Name
                        </label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            value={user.name}
                            onChange={handleChange}
                            className={`update-profile-input`}
                        />
                    </div>

                    <div className="update-profile-form-group">
                        <label htmlFor="phoneNumber" className="update-profile-label">
                            Phone Number
                        </label>
                        <input
                            type="tel"
                            id="phoneNumber"
                            name="phoneNumber"
                            value={user.phoneNumber}
                            onChange={handleChange}
                            className="update-profile-input"
                            placeholder="Optional"
                        />
                    </div>

                    <div className="update-profile-form-group">
                        <label htmlFor="password" className="update-profile-label">
                            New Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={user.password}
                            onChange={handleChange}
                            className="update-profile-input"
                            placeholder="Leave blank to keep current password"
                        />
              
                    </div>

                    <div className="update-profile-form-group">
                        <label htmlFor="confirmPassword" className="update-profile-label">
                            Confirm Password
                        </label>
                        <input
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            value={user.confirmPassword}
                            onChange={handleChange}
                            className="update-profile-input"
                            placeholder="Confirm new password"
                        />
                
                    </div>

                    <div className="update-profile-actions">
                        <button type="submit" className="update-profile-submit">
                            Save Changes
                        </button>
                        <Link to="/profile" className="update-profile-cancel">
                            Cancel
                        </Link>
                    </div>
                </form>

            </div>

        </div>
    )

}
export default UpdateProfilePage;
