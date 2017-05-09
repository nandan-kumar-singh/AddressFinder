# AddressFinder
a simple android class that find complete address(City, pincode, country, address line) from given address or latitude and longitude. 

//to get address from latitude and longitude
        new AddressFinder(28.623624, 77.058628) {
            @Override
            public void onGetAddress(Address address) {
                Log.d(TAG, "onGetAddress: " + address.toString());
            }
        }.execute();

        //OR to get address from address text use limke this
        new AddressFinder("Uttam nagar, new delhi") {
            @Override
            public void onGetAddress(Address address) {
                Log.d(TAG, "onGetAddress: " + address.toString());
            }
        }.execute();
        
        
the output result is :-

Address1: C-76 Captain Vijyant Thapar Marg,
City    : Noida,
County  : Gautam Buddh Nagar,
State   : Uttar Pradesh,
Country : India,
Pin     : 201301
