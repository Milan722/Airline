import { Navigate,useLocation } from "react-router-dom";
import ApiService from "./ApiService";

export const RouteGuard=({element:Component,allowedRoles})=>{
    const location=useLocation();

    let hasRequiredRole=false;

    if(!allowedRoles||allowedRoles.length===0){
        hasRequiredRole=false;
    }
    else{
        hasRequiredRole=allowedRoles.some(role=>{
            if(role==='ADMIN'){
                return ApiService.isAdmin();
            }else if(role==='PILOT'){
                return ApiService.isPilot();
            }else if(role==='CUSTOMER'){
                return ApiService.isCustomer();
            }

            return false;
        });
    }

    if(hasRequiredRole){
        return Component;
    }else{
        return <Navigate to="/login" replace state={{from:location}}/>;
    }


}