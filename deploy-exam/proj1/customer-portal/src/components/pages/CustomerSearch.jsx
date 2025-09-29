import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import Select from "react-select";

const CustomerSearch = () => {
    const [customerList, setCustomerList] = useState([]);
    const [selectedValue, setSelectedValue] = useState(null);

    useEffect(() => {
        const loadCustomerPoint = async () => {
            try {
                const apiBaseUri = import.meta.env.VITE_API_BASE_URI || '';
                const response = await fetch(`${apiBaseUri}/points`);
                if (response.ok) {
                    const data = await response.json();
                    setCustomerList(data);
                } else {
                    const data = await response.text();
                    console.error(response.status, data);
                }
            } catch (error) {
                console.error(error);
            }
        };
        loadCustomerPoint();
    }, []);

    return (
        <div className="wrap">
            {customerList ? (
                <div className="search-id">
                    <Select
                        instanceId="search-select-box"
                        value={selectedValue}
                        options={customerList.map(customer => ({
                            value: customer.customerId,
                            label: customer.customerId
                        }))}
                        onChange={(option) => {
                            if (option) {
                                setSelectedValue(option);
                            }
                        }}
                        noOptionsMessage={() => "IDが見つかりません"}
                        placeholder="IDを入力してください"
                        isSearchable={true}
                        className="custom-select"
                        // プレフィックスを追加して書くことで中の要素もいじれるようにする
                        classNamePrefix="custom-select"
                        components={{
                            // Defaultで表示されているセパレーターを消す
                            IndicatorSeparator: () => null,
                        }}
                    />
                    {selectedValue && (
                        <div className="select-id">
                            <p>{selectedValue.label}&nbsp;:</p>
                            <Link to={`/pointPortal/${selectedValue.value}`}>
                                &nbsp;詳細
                            </Link>
                        </div>
                    )}
                    {customerList.length === 0 && <p className="not-found">データが存在しません</p>}
                </div>
            ) : (
                <div className="loading"></div> // fetch遅延を回避
            )}
        </div>
    );
}

export default CustomerSearch;
