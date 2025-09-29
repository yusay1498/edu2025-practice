import '../../assets/styles/main.scss'
import { BrowserRouter, Route, Routes} from 'react-router-dom'
import CustomerSearch from "../pages/CustomerSearch.jsx";
import PointPortal from "../pages/PointPortal.jsx";

function App() {
  return (
    <BrowserRouter>
        <header className="header">
            <div className="header_logo"></div>
        </header>
        <Routes>
            <Route path="/" element={<CustomerSearch />} />
            <Route path="/pointPortal" element={<CustomerSearch />} />
            <Route path="/pointPortal/:customerId" element={<PointPortal />} />
            <Route path="*" element={<div>404 Not Found</div>} />
        </Routes>
    </BrowserRouter>
  )
}

export default App
