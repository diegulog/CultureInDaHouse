import axios from "axios";

function getShowList(){
    axios.get("/shows");
}


export{
    getShowList
}