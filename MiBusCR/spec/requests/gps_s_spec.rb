require 'rails_helper'

RSpec.describe "Gps_s", :type => :request do
  describe "GET /gps_s" do
    it "works! (now write some real specs)" do
      get gps_s_path
      expect(response).to have_http_status(200)
    end
  end
end
