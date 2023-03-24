import {initializeApp} from 'firebase/app';
import {getStorage, ref, uploadBytes} from "firebase/storage";
const { v4: uuidv4 } = require('uuid');


// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyCoORNwtL5cpWzs12yZEDTfp_dcky7ms-Q",
    authDomain: "cultureindahouse-group4.firebaseapp.com",
    projectId: "cultureindahouse-group4",
    storageBucket: "cultureindahouse-group4.appspot.com",
    messagingSenderId: "970168660996",
    appId: "1:970168660996:web:dd5acc317fb9d131d5b151",
    measurementId: "G-WBPBNQ89X0"
};
const app = initializeApp(firebaseConfig);

const storage = getStorage(app);

export const firebaseStorage = {
    async saveImage(file: File) {
        const filename = uuidv4(); // generar nombre unico
        const storageRef = ref(storage, filename + file.name);
        const snapshot = await uploadBytes(storageRef, file)
        return `https://firebasestorage.googleapis.com/v0/b/${snapshot.metadata.bucket}/o/${snapshot.metadata.fullPath}?alt=media`
    }
}
