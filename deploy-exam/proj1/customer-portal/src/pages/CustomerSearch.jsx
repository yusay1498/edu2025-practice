import React, { useState } from "react";
import { Link } from "react-router-dom";
import { useFetch } from "../hooks/useFetch";
import { fetchCustomers } from "../api/pointApi";
import SelectCustomer from "../components/SelectCustomer";

const CustomerSearch = () => {
  const { data: customerList, loading, error } = useFetch(fetchCustomers, []);
  const [selectedValue, setSelectedValue] = useState(null);

  if (loading) return <div className="loading">読み込み中...</div>;
  if (error) return <div className="error">エラー: {error.message}</div>;

  return (
    <div className="wrap">
      {customerList && customerList.length > 0 ? (
        <div className="search-id">
          <SelectCustomer
            customers={customerList}
            onSelect={(option) => setSelectedValue(option)}
          />
          {selectedValue && (
            <div className="select-id">
              <p>{selectedValue.label}&nbsp;:</p>
              <Link to={`/pointPortal/${selectedValue.value}`}>詳細</Link>
            </div>
          )}
        </div>
      ) : (
        <p className="not-found">データが存在しません</p>
      )}
    </div>
  );
};

export default CustomerSearch;
