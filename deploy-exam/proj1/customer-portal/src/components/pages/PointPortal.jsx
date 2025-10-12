import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";

const PointPortal = () => {
    const [customerDetail, setCustomerDetail] = useState(null);
    const {customerId} = useParams();

    useEffect(() => {
        const loadCustomerPoint = async () => {
            try {
                const apiBaseUri = import.meta.env.VITE_API_BASE_URI || '';
                const response = await fetch(`${apiBaseUri}/points/${customerId}`);
                if (response.ok) {
                    const data = await response.json();
                    setCustomerDetail(data);
                } else {
                    const data = await response.text();
                    console.error(response.status, data);
                }
            } catch (error) {
                console.error(error);
            }
        }
        loadCustomerPoint();
    }, [customerId]);

    return (
        <div className="wrap">
            {customerDetail ? (
                <div className="customer">
                    <div className="mitsukoshi">
                        <h1 className="mitsukoshi_name">&nbsp;</h1>
                        <h2 className="mitsukoshi_member">GROUP&nbsp;&nbsp;MEMBER</h2>
                    </div>
                    <p className="customer_id">{customerDetail.customerId}</p>
                    <div className="customer_point">
                        <p>エムアイポイント</p>
                        <div className="customer_point_num">
                            <p className="customer_point_num_main">{customerDetail.current_points}</p>
                            <p className="customer_point_num_unit">ポイント</p>
                        </div>
                    </div>
                </div>
            ) : (
                <div className="loading"></div> // fetch遅延を回避
            )}
        </div>
    )
}

export default PointPortal